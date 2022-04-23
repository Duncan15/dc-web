package api.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.RunningMode;
import format.RespWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DBUtil;
import util.RequestParser;
import util.Verifier;


@WebServlet(name = "TemplateServlet",urlPatterns = {"/api/datacrawling/task/template/*"})
public class TemplateServlet extends HttpServlet {
	/*
	for api: /api/datacrawling/task/template/all
	for api: /api/datacrawling/task/template/:id
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathParam= RequestParser.parsePath(request.getRequestURI(),2);
		Map<String,Object> data=new HashMap<>();

		String [] templateParams={"id", "patternName"};
		String [] param={"webId", "webName"};
		List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();
		if("template".equals(pathParam[0]) && "all".equals(pathParam[1])){//for task/template/all
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			try{
				String[][] res=util.DBUtil.select("website", param);
				for(int i=0;i<res.length;i++){
					//use webId to search in pattern
					String[][] template= DBUtil.select("pattern", templateParams, Integer.parseInt(res[i][0]));
					for(int j = 0; j < template.length; j++){
						Map<String,Object> content = new HashMap<>();
						content.put("taskID", res[i][0]);
						content.put("taskName", res[i][1]);
						content.put("templateID",template[j][0]);
						content.put("templateName",template[j][1]);
						dataList.add(content);
					}

					String[][] template_structed = DBUtil.select("pattern_structed", templateParams, Integer.parseInt(res[i][0]));
					for(int j = 0; j < template_structed.length; j++){
						Map<String,Object> content=new HashMap<>();
						content.put("taskID", res[i][0]);
						content.put("taskName", res[i][1]);
						content.put("templateID",template_structed[j][0]);
						content.put("templateName",template_structed[j][1]);
						dataList.add(content);
					}
				}
				response.getWriter().println(RespWrapper.build(dataList,dataList.size()));
			}catch(Exception e){
				data.put("msg","模板参数获取失败");
				response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
			}
		} else if("template".equals(pathParam[0])){//for task/template/:id
			Integer templateID = 0;
			Integer webId = 0;
			if ((webId = Verifier.verifyInt(request.getParameter("taskID"))) == null || (templateID = Verifier.verifyInt(pathParam[1])) == null) {
				data.put("msg", "id参数解析错误");
				response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
				return;
			}
			try{
				RunningMode r = RunningMode.ValueOf(DBUtil.select("website", new String[]{"runningMode"},webId)[0][0]);
				Map<String, Object> content = new HashMap<>();
				content.put("runningMode", r);
				if (r == RunningMode.unstructed) {
					String[][] template=util.DBUtil.select("pattern", new String[]{"webId", "patternName", "xpath"}, new String[]{"id"}, new String[]{templateID + ""});

					if(template != null && template.length>0) {
						String[] res = util.DBUtil.select("website", param, Integer.parseInt(template[0][0]))[0];
						content.put("taskID", res[0]);
						content.put("taskName", res[1]);
						content.put("templateName", template[0][1]);
						content.put("templateXpath",template[0][2]);
						content.put("templateID", templateID);
					}
					response.getWriter().println(RespWrapper.build(content));
				} else {
					String[][] template=util.DBUtil.select("pattern_structed", new String[]{"webId", "patternName", "xpath","formula","type","headerXPath"}, new String[]{"id"}, new String[]{templateID + ""});
					if(template != null && template.length>0) {
						String[] res = util.DBUtil.select("website", param, Integer.parseInt(template[0][0]))[0];
						content.put("taskID", res[0]);
						content.put("taskName", res[1]);
						content.put("templateName", template[0][1]);
						content.put("templateXpath",template[0][2]);
						content.put("templateID", templateID);
						content.put("templateFormula", template[0][3]);
						content.put("templateType", template[0][4]);
						content.put("templateHeaderXpath", template[0][5]);
					}
					response.getWriter().println(RespWrapper.build(content));
				}
			}catch(Exception e){
				data.put("msg","模板参数获取失败");
				response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
			}
		} else{
			response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
		}
    }


    /*
    for api: /api/datacrawling/task/template/:id
	for api: /api/datacrawling/task/template/new
     */


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String[] pathParam=RequestParser.parsePath(request.getRequestURI(),2);
		int templateID = 0;
		int webId = 0;
		Map<String,Object> data=new HashMap<>();
		if("template".equals(pathParam[0])){
		 	if(!"new".equals(pathParam[1])){//for task/template/:id
				try{
					templateID = Integer.parseInt(pathParam[1]);
				}catch (NumberFormatException e){
					response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
					return;
				}
			}
			webId = Integer.parseInt(request.getParameter("taskID"));
			RunningMode r = RunningMode.ValueOf(DBUtil.select("website", new String[]{"runningMode"},webId)[0][0]);

			//前端页面保证这些值均不为空，这里无需验证
			String templateName=request.getParameter("templateName");
			String templateXpath=request.getParameter("templateXpath");
			String templateType=request.getParameter("templateType");
			String templateHeaderXPath=request.getParameter("templateHeaderXpath");
			String templateFormula=request.getParameter("templateFormula");

			String[] p = {"patternName", "webId"};
			String[] pv = {templateName, webId + ""};

			String[] par = {"id"};
			String[] parValue = {templateID + ""};


			data.put("taskID",webId);
			data.put("templateName",templateName);
			data.put("templateXpath",templateXpath);
			String[] params = {"webId","patternName","xpath"};
			String[] paramsValue = {webId+"", templateName, templateXpath};


			String[] params_struct= {"webId","patternName","xpath","formula","type","headerXPath"};
			String[] paramsValue_struct = {webId+"", templateName, templateXpath,templateFormula,templateType,templateHeaderXPath};

			if("new".equals(pathParam[1])) {//for task/template/new
				 if(DBUtil.select("pattern", par,p, pv).length != 0||DBUtil.select("pattern_structed", par,p, pv).length != 0) {
					 data.put("msg", "该模板名称已使用，请重新输入");
					 response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
				 } else {
					 if (r == RunningMode.unstructed) {
						DBUtil.insert("pattern", params, paramsValue);
						response.getWriter().println(RespWrapper.build(data));
					 } else {
						 data.put("templateFormula",templateFormula);
						 data.put("templateType", templateType);
						 data.put("templateHeaderXPath",templateHeaderXPath);
						 DBUtil.insert("pattern_structed", params_struct, paramsValue_struct);
						 response.getWriter().println(RespWrapper.build(data));
					 }
				}
			} else if(!"new".equals(pathParam[1])) {//for task/template/:id
				if (r == RunningMode.unstructed) {
					DBUtil.update("pattern", params, paramsValue, par, parValue);
					response.getWriter().println(RespWrapper.build(data));
				} else {
					data.put("templateFormula", templateFormula);
					data.put("templateType", templateType);
					data.put("templateHeaderXPath", templateHeaderXPath);
					DBUtil.update("pattern_structed", params_struct, paramsValue_struct, par, parValue);
					response.getWriter().println(RespWrapper.build(data));
				}
			} else {
				data.clear();
				data.put("msg","操作失败");
				response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
			}
		}else {
			response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
		}

	}


	/**
	 * for invoking api: /api/datacrawling/task/template?ruleId=&templateId= by http method DELETE
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		request.setCharacterEncoding("UTF-8");

		int webId = Integer.parseInt(request.getParameter("ruleId"));
		String[][] webInfos = DBUtil.select("website", new String[]{"runningMode"}, webId);
		RunningMode runningMode = RunningMode.ValueOf(webInfos[0][0]);
		int templateID = Integer.parseInt(request.getParameter("templateId"));
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		try{
			if (runningMode == RunningMode.unstructed){
				DBUtil.delete("pattern",new String[]{"id"}, new String[]{templateID + ""});
			}
			else {
				DBUtil.delete("pattern_structed", new String[]{"id"}, new String[]{templateID + ""});
			}

			response.getWriter().println(RespWrapper.build("删除成功"));
		}catch (Exception e){
			response.getWriter().println(RespWrapper.build("删除异常"));
		}

	}
}
