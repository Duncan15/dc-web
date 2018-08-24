package api.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.DBUtil;
import format.RespWrapper;
@WebServlet(name = "LoginServlet",urlPatterns = {"/api/datacrawling/task/loginparam/*"})
public class LoginServlet extends HttpServlet{
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Map<String,Object> data=new HashMap<>();
        String url=request.getRequestURL().toString();
        int webId =Integer.parseInt(url.substring(url.lastIndexOf("/")+1));
        String loginURL=request.getParameter("loginURL");
        String userNameID=request.getParameter("userNameID");
        String passwordID=request.getParameter("passwordID");
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String[] p={"runningMode","driver"};
        String[] ans =DBUtil.select("website", p,webId)[0];
        
        String[] param = {"userParam","pwdParam","userName","password","loginUrl"};
		String[] paramValue = {userNameID,passwordID,username,password,loginURL};
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		try{
			if(DBUtil.update("website", param, paramValue, webId)){
                if("structed".equals(ans[0])&&"true".equals(ans[1])){
                    String loginButton=request.getParameter("loginButton");
                    String[] params = {"loginButton"};
                    String[] paramsValue = {loginButton};
                    DBUtil.update("structedParam", params, paramsValue, webId);
                    data.put("loginButton",loginButton);
                }
                data.put("loginURL",loginURL);
                data.put("userNameID",userNameID);
                data.put("passwordID",passwordID);
                data.put("username",username);
                data.put("password",password);
                response.getWriter().println(RespWrapper.build(data));
            }
            else {
                data.put("msg","登陆参数修改失败");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
            }
		}catch(Exception e){
            data.put("msg","登陆参数修改失败");
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
		}

	}
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPut(request,response);
    }
}
