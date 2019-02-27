package api.config;

import format.RespWrapper;
import util.DBUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mysqlURL=request.getParameter("mysqlURL");
        String mysqlUserName=request.getParameter("mysqlUserName");
        String mysqlPassword=request.getParameter("mysqlPassword");
        Properties prop=new Properties();
        prop.load(this.getClass().getResourceAsStream("/application.properties"));
        prop.setProperty("mysqlURL",mysqlURL);
        prop.setProperty("mysqlUserName",mysqlUserName);
        prop.setProperty("mysqlPassword",mysqlPassword);
        prop.store(new FileOutputStream(this.getClass().getResource("/application.properties").getFile()),"");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Properties prop=new Properties();
        prop.load(this.getClass().getResourceAsStream("/application.properties"));
        Map<String,String> ans=new HashMap<>();
        boolean flag=true;
        if(prop.getProperty("mysqlURL")!=null){
            ans.put("mysqlURL",prop.getProperty("mysqlURL"));
        }else {
            flag=false;
        }
        if(prop.getProperty("mysqlUserName")!=null){
            ans.put("mysqlUserName",prop.getProperty("mysqlUserName"));
        }else {
            flag=false;
        }
        if(prop.getProperty("mysqlPassword")!=null){
            ans.put("mysqlPassword",prop.getProperty("mysqlPassword"));
        }else {
            flag=false;
        }
        if(flag){
            response.getWriter().println(RespWrapper.build(ans));
        }else {
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,ans));
        }

    }
}
