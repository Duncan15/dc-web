package api.config;

import format.RespWrapper;
import services.ConfigService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "PlaConfigServlet", urlPatterns = "/api/datacrawling/config/platform")
public class PlaConfigServlet extends HttpServlet {
    /**
     * api for change platform configuration
     * @param request
     * @param response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        String baseWorkDir = request.getParameter("baseWorkDir");
        if (!new File(baseWorkDir).exists()) {
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, "基础工作目录不存在"));
            return;
        }
        ConfigService.setBaseWorkDir(baseWorkDir);
        response.getWriter().println(RespWrapper.build("success"));
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseWorkDir = ConfigService.getBaseWorkDir();
        Map<String,String> ans=new HashMap<>();
        ans.put("baseWorkDir",baseWorkDir);
        response.getWriter().println(RespWrapper.build(ans));
    }
}
