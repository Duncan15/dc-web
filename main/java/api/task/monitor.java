package api.task;

import net.sf.json.JSONObject;
import util.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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
    String taskId=request.getParameter("taskID");
    String option=request.getParameter("option");
    String param1[]={"runningMode"};
    String structType=DBUtil.select("website",param1 , Integer.parseInt(taskId))[0][0];

    JSONObject resultans = new JSONObject();
    int errno=0;
    if(option.equals("start")){
        //此处为调用start函数
        try {
            errno=0;
            start(request,structType, taskId);
        }
        catch (Exception e) {
            errno=500;
        }
        resultans.put("errno",errno);
        resultans.put("data"," ");
        response.getWriter().println(resultans);
    }
    else if(option.equals("stop")){

        //此处为stop函数
        try {
            errno=0;
            stop(request, taskId);
        }
        catch (Exception e) {
            errno=500;
        }
        resultans.put("errno",errno);
        resultans.put("data"," ");
        response.getWriter().write(resultans.toString());
        }

    }
}
