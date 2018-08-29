package api.task;
import format.RespWrapper;
import util.DBUtil;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "status",urlPatterns = {"/api/datacrawling/task/status"})
public class status extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String webId=request.getParameter("id");
        String[] param2={"M1status","M2status","M3status","M4status"};
        String currentStatus= DBUtil.select("current", param2,Integer.parseInt(webId))[0][0];
        String taskstatus="stoped";
        if(!currentStatus.equals("stoped")){
            taskstatus="started";
        }
        Map<String,Object> data=new HashMap<String, Object>();
        data.put("taskID",webId);
        data.put("taskStatus",taskstatus);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(RespWrapper.build(data));
        }
}
