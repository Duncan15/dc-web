package api.task;

import format.RespWrapper;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "myPut", urlPatterns = {"/api/datacrawling/request/*"})
public class myPut extends HttpServlet {

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        //前端页面保证这些值均不为空，这里无需验证
        StringBuffer path = request.getRequestURL();
        System.out.println(path);
        if("/api/datacrawling/request:id".equals(path)){


            String requestID=request.getParameter("requestID");
            String requestName = request.getParameter("requestName");
            String requestDesc = request.getParameter("requestDesc");


            String[] params = {"requestName", "requestDesc",};
            String[] params_value={requestName,requestDesc};
            boolean isUpdated= DBUtil.update("requesttable",params,params_value,Integer.parseInt(requestID));
            System.out.println("isUpdated ?:"+isUpdated);

            int ID = Integer.parseInt(requestID);
            Map<String, Object> data = new HashMap<>();
            data.put("requestID", ID);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(RespWrapper.build(data));

        } else {
            throw new RuntimeException("查无此页");
        }

    }

}