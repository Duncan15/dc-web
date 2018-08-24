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
@WebServlet(name = "PatternServlet",urlPatterns = {"/api/datacrawling/task/template/*"})
public class PatternServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   	 request.setCharacterEncoding("UTF-8");
         //前端页面保证这些值均不为空，这里无需验证
        String url=request.getRequestURL().toString();
        int webId =Integer.parseInt(url.substring(url.lastIndexOf("/")+1));
        String templateName=request.getParameter("templateName");
        String templateType=request.getParameter("templateType");
        String templateXpath=request.getParameter("templateXpath");	
        String templateFormula=request.getParameter("templateFormula");
        String templateHeaderXpath=request.getParameter("templateHeaderXpath");
        Map<String,Object> data=new HashMap<>();
		String indexPath = templateName+"index";
		String[] p = {"patternName","webId"};
		String[] pv = {templateName,webId+""};
		String[] par = {"webId"};
		
		String[] params = {"webId","patternName","xpath","indexPath","type","formula","headerXPath"};
		String[] params_value = {webId+"",templateName,templateXpath,indexPath,templateType,templateFormula,templateHeaderXpath};
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

		if(DBUtil.select("pattern", par,p, pv).length!=0) {
			data.put("msg", "该模板名称已使用，请重新输入");
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
		}
		else if(DBUtil.insert("pattern", params, params_value)){
            data.put("templateName",templateName);
            data.put("templateType",templateType);
            data.put("templateXpath",templateXpath);
            data.put("templateFormula",templateFormula);
            data.put("templateHeaderXpath",templateHeaderXpath);
            response.getWriter().println(RespWrapper.build(data));
		}
        else {
            data.put("msg","登陆参数修改失败");
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
        }

   }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
