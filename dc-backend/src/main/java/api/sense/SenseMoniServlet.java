package api.sense;

import format.RespWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.RequestParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import services.SenseThread;
@WebServlet(name = "SenseMoniServlet",urlPatterns = {"/api/datacrawling/sensemoni/*"})



public class SenseMoniServlet extends HttpServlet {


    private HashMap<String, SenseThread> threadMap = new HashMap<>();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String[] pathParam= RequestParser.parsePath(request.getRequestURI(),2);
        Map<String,Object> data=new HashMap<>();
        List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();

        if("sensemoni".equals(pathParam[0])&&"option".equals(pathParam[1])){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            String action=request.getParameter("action");
            String senseId=request.getParameter("senseId");
            System.out.println(action+"  ,,,,  "+senseId);
            if(action.equals("start")){

                if(threadMap.containsKey(senseId)){
                    if(threadMap.get(senseId).getStatus().equals("start")){
                        data.put("msg", "侦测任务: " + senseId + " 已经启动,请勿重复启动");
                    }else {

                        threadMap.remove(senseId);
                        SenseThread newThread = new SenseThread();
                        newThread.setName(senseId);
                        newThread.start();
                        threadMap.put(senseId,newThread);
                        data.put("msg", "启动侦测任务: " + senseId + " 成功");
                    }
                }else {
                    try {

                        SenseThread newThread = new SenseThread();
                        newThread.setName(senseId);
                        newThread.start();
                        threadMap.put(senseId,newThread);
                        data.put("msg", "启动侦测任务: " + senseId + " 成功");

                    }catch (Exception e){
                        threadMap.remove(senseId);
                    }



                }

            }else if(action.equals("stop")){
                /*
                 * stop a thread.*/
                if (!threadMap.containsKey(senseId)) {
                    data.put("msg", "侦测任务: " + senseId + "尚未启动");
                } else {

                    threadMap.get(senseId).setStatus("stop");
                    threadMap.get(senseId).interrupt();
                    threadMap.remove(senseId);
                    data.put("msg", "停止侦测任务: " + senseId);
                }


            }else {


                data.put("msg", "操作未定义,无法执行");
            }



        }
        response.getWriter().println(RespWrapper.build(data));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
