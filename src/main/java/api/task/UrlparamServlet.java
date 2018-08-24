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
import util.Config;

@WebServlet(name = "UrlparamServlet",urlPatterns = {"/api/datacrawling/task/urlparam/*"})
public class UrlparamServlet  extends HttpServlet{
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
		Map<String,Object> data=new HashMap<>();
		String url=request.getRequestURL().toString();
		int webId =Integer.parseInt(url.substring(url.lastIndexOf("/")+1));
        String[] p={"runningMode","driver","usable"};
        String[] ans =DBUtil.select("website", p,webId)[0];
        String runningMode=ans[0];
        String driver=ans[1];
		String usable="false";

		String flag =DBUtil.select("website", p,webId)[0][0];
		if(flag!=null ||flag.length()!=0)usable="true";
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
        if("false".equals(driver)){
        	System.out.println(driver);
	        String siteURL=request.getParameter("siteURL");
	        String searchURL=request.getParameter("searchURL");
	        String keywordName=request.getParameter("keywordName");
	        String pageParamName=request.getParameter("pageParamName");
	        String pageParamValue=request.getParameter("pageParamValue");
	        String otherParamName=request.getParameter("otherParamName");
	        String otherParamValue=request.getParameter("otherParamValue");
	        
	        String[]  param = {"indexUrl","prefix","paramQuery","paramPage","startPageNum","paramList","paramValueList","usable"};
	        String[] paramValue = {siteURL,searchURL,keywordName,pageParamName,pageParamValue,otherParamName,otherParamValue,usable};
	       
	        try{
				if(DBUtil.update("website", param, paramValue, webId)){
					data.put("siteURL",siteURL);
					data.put("searchURL",searchURL);
					data.put("keywordName",keywordName);
					data.put("pageParamName",pageParamName);
					data.put("otherParamName",otherParamName);
					data.put("pageParamValue",pageParamValue);
					data.put("otherParamValue",otherParamValue);
					if("structed".equals(runningMode)){
						String paramQueryValueList=request.getParameter("paramQueryValueList");
						String[] params = {"dataParamList"};
						String[] paramsValue = {paramQueryValueList};
						DBUtil.update("queryparam", params, paramsValue, webId);
						data.put("paramQueryValueList",paramQueryValueList);
					}
					response.getWriter().println(RespWrapper.build(data));
				} else {
					data.put("msg","登陆参数修改失败");
					response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
				}

	        }catch(Exception e){
				response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
			}
        }
        else if("structed".equals(runningMode)){
			System.out.println(runningMode);
        	String iframeNav = request.getParameter("iframeNav").trim();
			String indexUrl = request.getParameter("indexUrl").trim();
			String navValue = request.getParameter("navValue").trim();
			String iframeCon = request.getParameter("iframeCon").trim();
			String searchButton = request.getParameter("searchButton").trim();
			String resultRow = request.getParameter("resultRow".trim());
			String nextPageXPath = request.getParameter("nextPageXPath").trim();
			String pageNumXPath = request.getParameter("pageNumXPath").trim();
			String iframeSubParam = request.getParameter("iframeSubParam").trim();
			String arrow = request.getParameter("arrow").trim();
			String paramList = request.getParameter("paramList").trim();
			String paramValueList = request.getParameter("paramValueList").trim();


			String[] params = {"webId","iframeNav","navValue","iframeCon","searchButton","resultRow","nextPageXPath"
					,"pageNumXPath","iframeSubParam","arrow"};
			String[] paramsValue = {webId+"",iframeNav,navValue,iframeCon,searchButton,resultRow,
					nextPageXPath,pageNumXPath,iframeSubParam,arrow};

			String[] param = {"indexUrl","paramList","paramValueList","usable"};
			String[] paramValue = {indexUrl,paramList,paramValueList,usable};



			try{
				if(DBUtil.update("website", param, paramValue, webId)&& DBUtil.update("structedParam", params, paramsValue,webId)){
					data.put("iframeNav",iframeNav);
					data.put("navValue",navValue);
					data.put("iframeCon",iframeCon);
					data.put("searchButton",searchButton);
					data.put("resultRow",resultRow);
					data.put("nextPageXPath",nextPageXPath);
					data.put("pageNumXPath",pageNumXPath);
					data.put("iframeSubParam",iframeSubParam);
					data.put("arrow",arrow);
					data.put("paramList",paramList);
					data.put("paramValueList",paramValueList);
					response.getWriter().println(RespWrapper.build(data));
				} else {
					data.put("msg","链接参数修改失败");
					response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
				}
			}catch(Exception e){
				e.printStackTrace();
				response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
			}
        }
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPut(request,response);
	}
	

	
}
