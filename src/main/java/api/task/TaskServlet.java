package api.task;

import format.RespWrapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "TaskServlet",urlPatterns = {"/api/datacrawling/task"})
public class TaskServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        //前端页面保证这些值均不为空，这里无需验证
        String taskName=request.getParameter("taskName");
        String runningMode=request.getParameter("runningMode");
        String workPath=request.getParameter("workPath");
        //处理数据插入
        //
        //

        Map<String,Object> data=new HashMap<>();
        data.put("taskID","1");
        data.put("taskName",taskName);
        data.put("runningMode",runningMode);
        data.put("workPath",workPath);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(RespWrapper.build(data));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
