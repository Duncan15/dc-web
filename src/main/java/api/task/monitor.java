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

@WebServlet(name = "monitor",urlPatterns = {"/api/datacrawling/task/monitor"})
public class monitor extends HttpServlet {
    private HashMap<String, Process> processMap=new HashMap<String, Process>();
    private HashMap<String, ProcessBuilder> builderMap=new HashMap<String, ProcessBuilder>();


    private void start(HttpServletRequest request,String structType,String webid) throws IOException{
        ProcessBuilder builder=null;

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


        //didn't solve problem about structType unfit here
        request.setAttribute("信息", "爬虫成功启动");
        Process p=builder.start();
        processMap.put(webid, p);
        builderMap.put(webid, builder);

    }

    private void stop(HttpServletRequest request,String webid) throws IOException{
        Process p=null;
        p=processMap.get(webid);
        p.destroyForcibly();

//        processMap.remove(webid);
//        builderMap.remove(webid);

        request.setAttribute("信息", "爬虫成功启动");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String taskId=request.getParameter("taskID");
        String option=request.getParameter("option");
        String param1[]={"runningMode"};
        String structType=DBUtil.select("website",param1 , Integer.parseInt(taskId))[0][0];
        int errno=0;
        if(option.equals("start")){

            try {
                //此处为调用start函数
                start(request,structType,taskId);
                String data="";

                response.getWriter().println(RespWrapper.build(data));

            }catch(Exception e){
                String data="调用start函数错误";
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
            }

        }
        else if(option.equals("stop")){

            //此处为stop函数
            try {
                stop(request, taskId);
                String data="";
                response.getWriter().println(RespWrapper.build(data));

            }catch(Exception e){
                String data="调用stop函数错误";
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
            }
            }

    }
}
