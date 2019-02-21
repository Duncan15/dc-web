package api.task;

import format.RespWrapper;
import util.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "MonitorServlet",urlPatterns = {"/api/datacrawling/task/monitor"})
public class MonitorServlet extends HttpServlet {
    private HashMap<String, Process> processMap=new HashMap<String, Process>();


    private String start(HttpServletRequest request,String structType,String webid) throws IOException{
        ProcessBuilder builder=null;
        Process p=null;
        String ans;
        if(processMap.containsKey(webid)){
            p=processMap.get(webid);
            if(p.isAlive()){
                ans= "该爬虫已处于运行状态";
                return ans;
            }else {
                processMap.remove(webid);
            }
        }
        String usable=DBUtil.select("website",new String[]{"usable"},Integer.parseInt(webid))[0][0];
        if(!"true".equals(usable)){
            ans="当前配置不可用，请完善配置";
            return ans;
        }

         //此处为爬取的jar包path
//        String jarPath=new File(getServletContext().getRealPath("/"),"WEB-INF/lib/lp2.jar").getAbsolutePath();
//        builder=new ProcessBuilder("java","-jar",jarPath,webid);
        if("unstructed".equals(structType)){
            //WEB-INF/lib/ControllerNew.jar
            String jarPath=new File(getServletContext().getRealPath("/"),"WEB-INF/lib/local.jar").getAbsolutePath();
            builder=new ProcessBuilder("java","-Xmx200G","-Xms20G","-jar",jarPath,webid);
        }
        else if("structed".equals(structType)){
            String jarPath=new File(getServletContext().getRealPath("/"),"WEB-INF/lib/Controller_structured.jar").getAbsolutePath();
            builder=new ProcessBuilder("java","-jar",jarPath,webid);
        }
        else if("structed_js".equals(structType)){
            String jarPath=new File(getServletContext().getRealPath("/"),"WEB-INF/lib/Controller_structured_js.jar").getAbsolutePath();
            builder=new ProcessBuilder("java","-jar",jarPath,webid);
        }
        p=builder.start();
        try{
            Thread.sleep(1000l);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        if(p.isAlive()){
            ans="爬虫启动成功";
            processMap.put(webid, p);
        }else {
            ans="爬虫启动失败，请检查参数是否配置正确";
        }
        return ans;

    }

    private String stop(HttpServletRequest request,String webid){
        Process p=null;
        String ans;
        if(processMap.containsKey(webid)){
            p=processMap.get(webid);
            if(!p.isAlive()){
                ans="爬虫未处于运行状态";
            }else {
                p.destroyForcibly();
                try{
                    Thread.sleep(1000l);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                if(p.isAlive()){
                     ans="爬虫未正常停止";
                }else {
                    DBUtil.update("current",new String[]{"M1status","M2status","M3status","M4status"},new String[]{"stoped","stoped","stoped","stoped"},Integer.parseInt(webid));
                    ans="爬虫正常停止";
                    processMap.remove(webid);
                }
            }
        }else {
            ans = "系统未检索到爬虫运行信息，无法停止";
        }
        return ans;
    }
    /*
    for api: /api/datacrawling/task/monitor?action=status
    for api: /api/datacrawling/task/monitor?action=option&option=start or stop&taskID=1
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String action=request.getParameter("action");
        if(action.equals("status")){
            List content=new ArrayList();
            String[][] website=DBUtil.select("website",new String[]{"webId","webName","databaseSize"});
            Map<String,Integer> idNameMap=new HashMap<>();
            for(int i=0;i<website.length;i++){
                idNameMap.put(website[i][0],i);
            }
            String[][] current=DBUtil.select("current",new String[]{"webId","round","M1status","M2status","M3status","M4status","fQueryLink_sum","fInfoLink_sum","SampleData_sum"});
            for(int i=0;i<current.length;i++){
                Map<String,String> unit=new HashMap<>();
                unit.put("taskID",current[i][0]);
                unit.put("taskName",website[idNameMap.get(current[i][0])][1]);
                unit.put("round",current[i][1]);
                unit.put("fQueryLinkSum",current[i][6]);
                unit.put("fInfoLinkSum",current[i][7]);
                unit.put("sampleDataSum",current[i][8]);
                if(website[idNameMap.get(current[i][0])][2]==null||"0".equals(website[idNameMap.get(current[i][0])][2])){
                    unit.put("crawlRatio","未知");
                }else {
                    float sampleNum=Float.parseFloat(current[i][8]);
                    float dbNum=Float.parseFloat(website[idNameMap.get(current[i][0])][2]);
                    unit.put("crawlRatio",sampleNum/dbNum*100+"%");
                }
                String taskStatus="未启动";
                if(processMap.containsKey(current[i][0])){
                    Process p=processMap.get(current[i][0]);
                    if(p.isAlive()){
                        for(int j=2;j<6;j++){
                            if(current[i][j].equals("active")){
                                switch (j){
                                    case 2:
                                        taskStatus="获取数据链接";
                                        break;
                                    case 3:
                                        taskStatus="下载数据";
                                        break;
                                    case 4:
                                        taskStatus="模版数据抽取";
                                        break;
                                    case 5:
                                        taskStatus="获取关键词";
                                        break;
                                }
                                break;
                            }
                        }
                    }
                }
                unit.put("status",taskStatus);
                content.add(unit);
            }
            //String[] param2={"M1status","M2status","M3status","M4status"};
            //String currentStatus= DBUtil.select("current", param2,Integer.parseInt(taskId))[0][0];

            //if(!currentStatus.equals("stoped")){
            //    taskstatus="started";
            //}
            Map<String,Object> data=new HashMap<String, Object>();
            data.put("content",content);
            data.put("total",content.size());
            response.getWriter().println(RespWrapper.build(data));
        }else if(action.equals("option")){
            String taskId=request.getParameter("taskID");
            String option=request.getParameter("option");
            String param1[]={"runningMode"};
            String structType=DBUtil.select("website",param1 , Integer.parseInt(taskId))[0][0];
            Map<String,Object> data=new HashMap<>();
            if(option.equals("start")){
                //此处为调用start函数
                String msg=start(request,structType,taskId);
                data.put("msg",msg);
                response.getWriter().println(RespWrapper.build(data));
            }
            else if(option.equals("stop")) {
                //此处为stop函数
                String msg = stop(request, taskId);
                data.put("msg", msg);
                response.getWriter().println(RespWrapper.build(data));
            }
        }
    }
}
