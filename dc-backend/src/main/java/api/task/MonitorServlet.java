package api.task;

import enums.*;
import format.RespWrapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ConfigService;
import util.DBUtil;
import util.FileZip;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@WebServlet(name = "MonitorServlet",urlPatterns = {"/api/datacrawling/task/monitor"})
public class MonitorServlet extends HttpServlet {

    private String start(RunningMode runningMode, Base base, Driver driver, int webID) throws IOException{
        ProcessBuilder builder=null;
        String[][] ans = DBUtil.select("current", new String[]{"run"}, webID);
        if(ans.length != 0 &&!ans[0][0].equals("0")) {
            return "该爬虫正处于运行状态，禁止重复操作";
        }

        String[] websiteInfo=DBUtil.select("website",new String[]{"usable","workFile"}, webID)[0];
        String usable = websiteInfo[0];
        Usable u = Usable.valueOf(Integer.parseInt(usable));
        if(u == Usable.none){
            return "当前配置不可用，请先完善配置";
        }

        Properties properties = ConfigService.getBackMap();
        String mysqlURL = properties.getProperty("mysqlURL");
        String mysqlUserName = properties.getProperty("mysqlUserName");
        String msyqlPassword = properties.getProperty("mysqlPassword");

        if(runningMode == RunningMode.unstructed && (base == Base.urlBased || base == Base.apiBased || base == Base.jsonBased)){
//            --web-id=116
//            --jdbc-url=jdbc:mysql://localhost:3306/webcrawler?characterEncoding=UTF-8&useSSL=false&useAffectedRows=true&allowPublicKeyRetrieval=true
//            --username=root
//            --password=12345678
            String jarPath = new File(getServletContext().getRealPath("/"),"WEB-INF/lib/Controller_unstructed.jar").getAbsolutePath();
            builder = new ProcessBuilder("java", "-jar", jarPath, "--web-id=" + webID, "--jdbc-url=" + mysqlURL, "--username=" + mysqlUserName, "--password=" + msyqlPassword);

            //设置工作目录，主要作用是支持ansj的配置载入
            builder.directory(new File(getServletContext().getRealPath("/"), "WEB-INF"));
        } else if(runningMode == RunningMode.structed && (driver == Driver.json||driver == Driver.have||driver == Driver.none)){//以下启动模式根据自定义进行修改
            String jarPath = new File(getServletContext().getRealPath("/"),"WEB-INF/lib/Controller_structed.jar").getAbsolutePath();
            builder = new ProcessBuilder("java","-jar",jarPath, webID+"",  mysqlURL,  mysqlUserName, msyqlPassword);
        }
        if (builder == null) {
            return "爬虫启动失败，该爬虫属于未知类型，请检查配置";
        }


        File logFile = Paths.get(websiteInfo[1], webID + "", ConfigService.LOG_FILE).toFile();
        File logErr = Paths.get(websiteInfo[1], webID + "", ConfigService.LOG_ERR).toFile();
        if(logFile.exists()) {
            logFile.delete();
        }
        if(logErr.exists()) {
            logErr.delete();
        }



        builder.redirectOutput(logFile);
        builder.redirectError(logErr);

        Process p = builder.start();
        try{
            Thread.sleep(5000);
        }catch (InterruptedException e){
            //ignored
        }
        if (p.isAlive()) {
            return "爬虫成功启动";
        } else if (p.exitValue() == 0) {
            return "爬虫启动，检测到目标网站数据无更新，爬虫退出";
        } else {
            return "爬虫启动失败,程序退出码为" + p.exitValue() + "，请重新检查参数配置是否正确，或查看输出日志进行问题定位";
        }

    }

    private String stop(int webID){
        String[][] ans = DBUtil.select("current", new String[]{"run"}, webID);
        if (ans.length == 0) {
            return "webID所对应的网站不存在";
        }
        if(ans[0][0].equals("0")) {
            return "该爬虫正处于未启动状态，无权进行暂停操作";
        }
        long pid = Long.parseLong(ans[0][0]);
        Runtime rt = Runtime.getRuntime();
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
            try {
                // /f force to kill the specified process
                // /t kill the specified process and it's sub process
                //this method would let the specified process to execute the shutdown hook, so this is not a good method
                rt.exec("taskkill /f /t /pid " + pid);
                DBUtil.update("current", new String[]{"run"}, new String[]{"0"}, webID);
            } catch (IOException ex) {
                //ignored
            }
        } else {
            try {
                rt.exec("kill " + pid);
            } catch (IOException ex) {
                //ignored
            }
        }
        try {
            Thread.sleep(2000l);
        } catch (InterruptedException ex) {
            //ignored
        }
        ans = DBUtil.select("current", new String[]{"run"}, webID);
        if (ans[0][0].equals("0")) {
            return "爬虫成功暂停";
        } else {
            return "爬虫暂停异常，请稍后重试，或查看输出日志进行问题定位";
        }
    }
    /*
    for api: /api/datacrawling/task/monitor?action=status
    for api: /api/datacrawling/task/monitor?action=option&option=start or stop&taskID=1
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String,Object> data=new HashMap<>();
        String action=request.getParameter("action");
        if(action.equals("status")){//monitor?action=status
            List content=new ArrayList();
            //只显示配置可用的爬虫状态
            String[][] website = DBUtil.select("website",new String[]{"webId", "webName", "runningMode", "driver", "base"}, new String[]{"usable"}, new String[]{Usable.have.getValue() + ""});
            Map<String,Integer> idNameMap = new HashMap<>();
            for(int i = 0; i < website.length; i++){
                idNameMap.put(website[i][0], i);
            }

            String[] p = new String[]{"webId", "databaseSize"};
            String[][] ans = DBUtil.select("extraConf", p);
            Map<String, Long> sizeMap = new HashMap<>();
            for (int i = 0; i < ans.length; i++) {
                sizeMap.put(ans[i][0], Long.parseLong(ans[i][1]));
            }

            String[][] current = DBUtil.select("current",new String[]{"webId","round","M1status","M2status","M3status","M4status","SampleData_sum", "run"});

            for(int i = 0; i < current.length; i++){
                Map<String,String> unit = new HashMap<>();
                unit.put("taskID", current[i][0]);
                unit.put("taskName", website[idNameMap.get(current[i][0])][1]);
                unit.put("round", current[i][1]);
                unit.put("sampleDataSum", current[i][6]);

                //如果databaseSize值为0，则爬取比例为未知状态
                if (sizeMap.get(current[i][0]) == null || sizeMap.get(current[i][0]) == 0){
                    unit.put("crawlRatio", "未知");
                } else {
                    float sampleNum = Float.parseFloat(current[i][6]);
                    float dbNum = sizeMap.get(current[i][0]);
                    unit.put("crawlRatio", sampleNum / dbNum * 100 + "%");
                }
                Properties properties = ConfigService.getBackMap();
                String[] websiteRow = website[idNameMap.get(current[i][0])];
                RunningMode runningMode = RunningMode.ValueOf(websiteRow[2]);
                if (runningMode == RunningMode.structed)
                    unit.put("round",(Integer.parseInt(current[i][1])+1)+"" );
                if ("0".equals(current[i][7])) {
                    unit.put("status", "已停止");
                } else {

                    Driver driver = Driver.valueOf(Integer.parseInt(websiteRow[3]));
                    Base base = Base.valueOf(Integer.parseInt(websiteRow[4]));
                    String status = "已启动";
                    if (runningMode == RunningMode.unstructed) {

                        for (int j = 2; j < 6; j++) {
                            if(current[i][j].equals("active")) {
                                status = properties.getProperty(runningMode.name() + "." + base.name() + "." +"status" + (j - 1));
                                break;
                            }
                        }

                    } else if (runningMode == RunningMode.structed) {

                        for (int j = 2; j < 6; j++) {
                            if(current[i][j].equals("active")) {
                                status = properties.getProperty(runningMode.name() + "." +driver.name() + "." +"status" + (j - 1));
                                break;
                            }
                        }
                    }
                    unit.put("status", status);
                }
                content.add(unit);
            }
            data.put("content",content);
            data.put("total",content.size());
            response.getWriter().println(RespWrapper.build(data));
            return;
        }else if(action.equals("option")){
            String taskIDStr = request.getParameter("taskID");
            String option = request.getParameter("option");

            Integer webID = 0;
            MonitorOption monitorOption = null;
            try {
                webID = Integer.parseInt(taskIDStr);
            } catch (NumberFormatException ex) {
                //ingored
                data.put("msg", "taskID参数格式错误");
                response.getWriter().println(RespWrapper.build(data));
                return;
            }
            String param1[] = {"runningMode", "driver", "base", "workFile"};
            String[][] ans =DBUtil.select("website",param1 , webID);
            if(ans.length == 0) {
                data.put("msg", "webID所对应的网站不存在");
                response.getWriter().println(RespWrapper.build(data));
                return;
            }
            RunningMode runningMode = RunningMode.ValueOf(ans[0][0]);
            Driver driver = Driver.valueOf(Integer.parseInt(ans[0][1]));
            Base base = Base.valueOf(Integer.parseInt(ans[0][2]));
            monitorOption = MonitorOption.valueOf(option);
            if (monitorOption == MonitorOption.start) {
                String msg = start(runningMode, base, driver, webID);
                data.put("msg", msg);
            } else if (monitorOption == MonitorOption.stop) {
                String msg = stop(webID);
                data.put("msg", msg);
                //对非结构型，在停止指令执行之后立即进行打包
                if (runningMode == RunningMode.unstructed) {
                    Path parAddr = Paths.get(ans[0][3], webID + "", "index");
                    Path sourceAddr = parAddr.resolve("fulltext");
                    Path zipAddr = parAddr.resolve(webID + ".zip");
                    if (sourceAddr.toFile().isDirectory()) {
                        zipAddr.toFile().delete();
                        new Thread() {
                            @Override
                            public void run() {
                                System.out.println("开始压缩");
                                FileZip.fileZip(sourceAddr.toString(), zipAddr.toString(), "lucene",true);
                                System.out.println("压缩完成");
                            }
                        }.start();
                    }

                }
            } else {
                data.put("msg", "该操作未定义，无法执行");
            }
            response.getWriter().println(RespWrapper.build(data));
            return;
        }
    }
}
