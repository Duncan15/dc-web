package api.task;

import format.RespWrapper;
import util.Config;
import util.DBUtil;

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
        String driver=request.getParameter("driver");

        String taskID="";
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> data=new HashMap<>();
        try {
            if (Config.checkTask(taskName)) {//if have existed the website in database
                data.put("msg", "该任务名称已使用，请重新输入");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
            } else if (Config.setInterface(runningMode, taskName, workPath, driver)) {
                taskID = DBUtil.getLastWebId() + "";
                data.put("taskID", taskID);
                data.put("taskName", taskName);
                data.put("runningMode", runningMode);
                data.put("workPath", workPath);

                response.getWriter().println(RespWrapper.build(data));
            }

        }catch(Exception e){
            data.put("msg","任务添加失败");
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
