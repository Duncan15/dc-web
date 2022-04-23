package api.request;

import format.RespWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DBUtil;
import util.RequestParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(name = "RequestServlet", urlPatterns = {"/api/datacrawling/request/*"})
public class RequestServlet extends HttpServlet {
    /*
     for api :/api/datacrawling/request/all
      */
    /*
     for api :/api/datacrawling/request/:id
      */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] pathParam = RequestParser.parsePath(request.getRequestURI(), 2);
        if ("request".equals(pathParam[0]) && "all".equals(pathParam[1])) {//for request/all
            String[] params = {"requestID", "requestName", "requestDesc", "createdTime"};
            String[][] requesttable = DBUtil.select("requesttable", params);
            int total = requesttable.length;
            List<Map<String, Object>> strings = new ArrayList<>();
            for (int i = 0; i < requesttable.length; i++) {
                Map<String, Object> data = new HashMap<>();
                data.put(params[0], Integer.parseInt(requesttable[i][0]));
                data.put(params[1], requesttable[i][1]);
                data.put(params[2], requesttable[i][2]);
                data.put(params[3], requesttable[i][3]);
                strings.add(data);
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(RespWrapper.build(strings, total));
        } else if ("request".equals(pathParam[0])) {//for request/:id
            int requestID = 0;
            try {
                requestID = Integer.parseInt(pathParam[1]);
            } catch (NumberFormatException e) {
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, null));
                return;
            }
            String[] params = {"requestID", "requestName", "requestDesc"};
            String[] conParams={"requestID"};
            String[] conPalues={String.valueOf(requestID)};
            String[][] requestData = DBUtil.select("requesttable", params, conParams, conPalues);
            Map<String, Object> data = new HashMap<>();
            data.put("requestID",Integer.parseInt(requestData[0][0]));
            data.put("requestName",requestData[0][1]);
            data.put("requestDesc",requestData[0][2]);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(RespWrapper.build(data));
        }
        else

        {
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, null));
        }

    }
     /*
        for api :/api/datacrawling/request/new
     */
     /*
            for api :/api/datacrawling/request/:id
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        //前端页面保证这些值均不为空，这里无需验证
        String[] pathParam = RequestParser.parsePath(request.getRequestURI(), 2);
        if ("request".equals(pathParam[0]) && "new".equals(pathParam[1])) {//for request/new
            String taskName = request.getParameter("requestName");
            String runningMode = request.getParameter("requestDesc");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String createdTime = df.format(new Date()).toString();
            //处理数据插入
            String[] params = {"requestName", "requestDesc", "createdTime"};
            String[] con_values = {taskName, runningMode, createdTime};
            boolean isInserted = DBUtil.insert("requesttable", params, con_values);
            int requestID = DBUtil.getLasttaskID("requesttable","requestID");
            Map<String, Object> data = new HashMap<>();
            data.put("requestID", requestID);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(RespWrapper.build(data));
        } else if("request".equals(pathParam[0])){//for request/:id
            int requestID = 0;
            try {
                requestID = Integer.parseInt(pathParam[1]);
            } catch (NumberFormatException e) {
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, null));
                return;
            }
            request.setCharacterEncoding("UTF-8");
            String requestName = request.getParameter("requestName");
            String requestDesc = request.getParameter("requestDesc");
            String[] params = {"requestName", "requestDesc",};
            String[] params_value = {requestName, requestDesc};
            String[] con_params={"requestID"};
            String[] con_vals={requestID+""};
            boolean isUpdated = DBUtil.update("requesttable", params, params_value, con_params,con_vals);
            Map<String, Object> data = new HashMap<>();
            data.put("requestID", requestID);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(RespWrapper.build(data));
        } else {
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, null));
        }

    }
}
