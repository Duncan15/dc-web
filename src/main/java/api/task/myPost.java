package api.task;

import format.RespWrapper;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "myPost", urlPatterns = {"/api/datacrawling/request"})
public class myPost extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        //前端页面保证这些值均不为空，这里无需验证
        String taskName = request.getParameter("requestName");
        String runningMode = request.getParameter("requestDesc");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String createdTime=df.format(new Date()).toString();
        //处理数据插入
        //
        //
        String[] params = {"requestName", "requestDesc", "createdTime"};
        String[] con_values = {taskName, runningMode, createdTime};
        boolean isInserted = DBUtil.insert("requesttable", params, con_values);
        System.out.println("insert or not ?"+isInserted);
        int requestID = DBUtil.getLasttaskID();
        Map<String, Object> data = new HashMap<>();
        data.put("requestID", requestID);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(RespWrapper.build(data));
    }





}
