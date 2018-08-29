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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "MonitorServlet",urlPatterns = {"/api/datacrawling/task/monitor"})
public class MonitorServlet extends HttpServlet {
    private HashMap<String, Process> processMap=new HashMap<String, Process>();


    private String start(HttpServletRequest request,String structType,String webid) throws IOException{
        ProcessBuilder builder=null;
        Process p=null;
        if(processMap.containsKey(webid)){
            p=processMap.get(webid);
            if(p.isAlive()){
                return "该爬虫已处于运行状态";
            }else {
                processMap.remove(webid);
            }
        }

         //此处为爬取的jar包path
//        String jarPath=new File(getServletContext().getRealPath("/"),"WEB-INF/lib/lp2.jar").getAbsolutePath();
//        builder=new ProcessBuilder("java","-jar",jarPath,webid);
        if("unstructed".equals(structType)){
            String jarPath=new File(getServletContext().getRealPath("/"),"WEB-INF/lib/ControllerNew.jar").getAbsolutePath();
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
        String ans;
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
    for api: /api/datacrawling/task/monitor?action=option&option=start or stop
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String taskId=request.getParameter("taskID");
        String action=request.getParameter("action");
        if(action=="status"){

        }else if(action=="option"){
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
