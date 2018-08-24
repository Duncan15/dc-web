package api.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import format.RespWrapper;
import util.DBUtil;

@WebServlet(name = "DownloadServlet",urlPatterns = {"/api/datacrawling/task/downloadparam/*"})
public class DownloadServlet extends HttpServlet{
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String url=request.getRequestURL().toString();

        int webId =Integer.parseInt(url.substring(url.lastIndexOf("/")+1));
        Map<String,Object> data=new HashMap<>();
        String threadNum=request.getParameter("threadNum");
        System.out.println(webId);
        String timeout=request.getParameter("timeout");
        String charset=request.getParameter("charset");
        String datagross=request.getParameter("datagross");
        String usable="false";
        String[] p={"indexUrl"};
        String ans =DBUtil.select("website", p,webId)[0][0];
        if(ans!=null ||ans.length()!=0)usable="true";
        String[] param = {"threadNum","timeout","charset","databaseSize","usable"};
		String[] paramValue = {threadNum,timeout,charset,datagross,usable};


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		try{
			if(DBUtil.update("website", param, paramValue, webId)){
                data.put("threadNum",threadNum);
                data.put("timeout",timeout);
                data.put("charset",charset);
                data.put("datagross",datagross);
                response.getWriter().println(RespWrapper.build(data));
            }
            else {
                data.put("msg","下载参数修改失败");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
            }
		}catch(Exception e){
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
		}


	}
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPut(request,response);
    }
}
