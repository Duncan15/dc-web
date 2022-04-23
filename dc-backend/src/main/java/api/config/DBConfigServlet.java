package api.config;

import format.RespWrapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ConfigService;
import util.DBUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@WebServlet(name = "DBConfigServlet",urlPatterns = {"/api/datacrawling/config/mysql"})
public class DBConfigServlet extends HttpServlet {
    /**
     * api for changing db configuration
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mysqlURL=request.getParameter("mysqlURL");
        String mysqlUserName=request.getParameter("mysqlUserName");
        String mysqlPassword=request.getParameter("mysqlPassword");
        ConfigService.setDB(mysqlURL, mysqlUserName, mysqlPassword);
        if (DBUtil.config(mysqlURL,mysqlUserName,mysqlPassword)){
            response.getWriter().println(RespWrapper.build("success"));
        }else {
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,"param error"));
        }

    }

    /**
     * api for get db configuration
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Properties prop = ConfigService.getBackMap();
        Map<String,String> ans=new HashMap<>();
        ans.put("mysqlURL",prop.getProperty("mysqlURL"));
        ans.put("mysqlUserName",prop.getProperty("mysqlUserName"));
        ans.put("mysqlPassword",prop.getProperty("mysqlPassword"));
        response.getWriter().println(RespWrapper.build(ans));


    }
}
