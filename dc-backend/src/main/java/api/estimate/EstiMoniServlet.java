package api.estimate;

import format.RespWrapper;
import services.ConfigService;
import util.DBUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@WebServlet(name = "EstiMoniServlet", urlPatterns = {"/api/datacrawling/estimoni"})
public class EstiMoniServlet extends HttpServlet {


    private String start(String estiId) throws IOException {
        ProcessBuilder builder = null;
        String curStatus = DBUtil.select("estimate", new String[]{"status"}, new String[]{"estiId"}, new String[]{estiId})[0][0];
        if (curStatus.equals("start")) {
            return "该估测任务正处于运行状态，禁止重复操作";
        }

        Properties properties = ConfigService.getBackMap();
        String mysqlURL = properties.getProperty("mysqlURL");
        String mysqlUserName = properties.getProperty("mysqlUserName");
        String mysqlPassword = properties.getProperty("mysqlPassword");


        DBUtil.update("estimate", new String[]{"status"}, new String[]{"start"}, new String[]{"estiId"}, new String[]{estiId});

        //此处为爬取的jar包path
        String jarPath = new File(getServletContext().getRealPath("/"), "WEB-INF/lib/Controller_estimate.jar").getAbsolutePath();
        builder = new ProcessBuilder("java", "-Xmx1024m", "-Xms256m", "-jar", jarPath, mysqlURL, mysqlUserName, mysqlPassword, estiId);
//
//
        File logFile = Paths.get("logFile+"+estiId+".txt").toFile();
        File logErr =  Paths.get("logErr+"+estiId+".txt").toFile();
        if (logFile.exists()) {
            logFile.delete();
        }
        if (logErr.exists()) {
            logErr.delete();
        }

        builder.redirectOutput(logFile);
        builder.redirectError(logErr);

        Process p = builder.start();
        try {
            Thread.sleep(5000l);
        } catch (InterruptedException e) {
            //ignored
        }
        if (p.isAlive()) {
            return "估测任务成功启动";
        } else {
            return "估测任务启动失败，请重新检查参数配置是正确，或查看输出日志进行问题定位";
        }

    }

    private String stop(String estiId) {
        String[][] ans = DBUtil.select("estimate", new String[]{"status"},new String[]{"estiId"}, new String[]{estiId} );
        if (ans.length == 0) {
            return "estimate id 所对应的网站不存在";
        }
        if (ans[0][0].equals("stop")) {
            return "该估测任务正处于未启动状态，无权进行暂停操作";
        }
        if (ans[0][0].equals("")||ans[0][0].isEmpty()){
            return "估测任务状态不明，无法进行停止操作";
        }

        String pidStr= DBUtil.select("estimate", new String[]{"pid"},new String[]{"estiId"}, new String[]{estiId})[0][0];
        System.out.println("pidStr is "+pidStr);
        long pid = Long.parseLong(pidStr);
        System.out.println("try to kill "+pid);

        Runtime rt = Runtime.getRuntime();
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
            try {
                rt.exec("taskkill /pid " + pid+" -f");
            } catch (IOException ex) {

                //ignored
            }
        } else {
            //在linux系统下的执行。
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

        DBUtil.update("estimate", new String[]{"status"}, new String[]{"stop"}, new String[]{"estiId"}, new String[]{estiId});

        ans = DBUtil.select("estimate", new String[]{"status"},new String[]{"estiId"}, new String[]{estiId} );
        if (ans[0][0].equals("stop")) {
            return "估测任务成功暂停";
        } else {
            return "估测任务暂停异常，请稍后重试，或查看输出日志进行问题定位";
        }
    }




    /*
    for api: /api/datacrawling/estimoni?action=status
    for api: /api/datacrawling/estimoni?action=option&option=start or stop&estiId=1
     */

    /*To store the threads that already start
     * in order to remove them when stop */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String action = request.getParameter("action");
        Map<String, Object> data = new HashMap<>();
        if (action.equals("status")) {



        } else if (action.equals("option")) {
            String estiIdStr = request.getParameter("estiId");
            String option = request.getParameter("option");//option=start or stop;

            /*To check if the estiId is a real number*/

            if (option.equals("start")) {
              String info=start(estiIdStr);
              data.put("msg",info);
                System.out.println(info);
            } else if (option.equals("stop")) {
               String stopInfo=stop(estiIdStr);
                data.put("msg",stopInfo);
                System.out.println("stopInfo is "+stopInfo);
            } else {
                data.put("msg", "操作未定义,无法执行");
            }
            response.getWriter().println(RespWrapper.build(data));
        }
    }


}
