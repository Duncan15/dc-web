package api.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import format.RespWrapper;
import util.DBUtil;
import util.RequestParser;


@WebServlet(name = "TemplateServlet",urlPatterns = {"/api/datacrawling/task/template/*"})
public class TemplateServlet extends HttpServlet {
	/*
	for api: /api/datacrawling/task/template/all
	for api: /api/datacrawling/task/template/:id
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathParam= RequestParser.parsePath(request.getRequestURI(),2);
		if("template".equals(pathParam[0])&&"all".equals(pathParam[1])){
			String [] template_params={"patternName","type"};//usable:true ,"formula","headerXPath"
			String [] param={"webId","webName"};
			List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();
			Map<String,Object> data=new HashMap<>();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			try{
				String[][] res=util.DBUtil.select("website", param);
				for(int i=0;i<res.length;i++){
					String[][] template=util.DBUtil.select("pattern", template_params, Integer.parseInt(res[i][0]));
					if(template.length>0){
						for(int j=0;j<template.length;j++){
							Map<String,Object> content=new HashMap<>();
							content.put("taskID", res[i][0]);
							content.put("taskName", res[i][1]);
							content.put("templateName",template[j][0]);
							content.put("templateType",template[j][1]);
							dataList.add(content);
						}
					}
				}
				response.getWriter().println(RespWrapper.build(dataList,dataList.size()));
			}catch(Exception e){
				data.put("msg","模板参数修改失败");
				response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
			}
		}else {
			response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
		}
    }


    /*
    for api: /api/datacrawling/task/template/:id
	for api: /api/datacrawling/task/template/new
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String[] pathParam=RequestParser.parsePath(request.getRequestURI(),2);
		if("template".equals(pathParam[0])){
			int taskID=0;
			try{
				taskID=Integer.parseInt(pathParam[1]);
			}catch (NumberFormatException e){
				response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
				return;
			}
			Map<String,Object> data=new HashMap<>();
			int webId =taskID;
			//前端页面保证这些值均不为空，这里无需验证
			String templateName=request.getParameter("templateName");
			String templateType=request.getParameter("templateType");
			String templateXpath=request.getParameter("templateXpath");
			String templateFormula=request.getParameter("templateFormula");
			String templateHeaderXpath=request.getParameter("templateHeaderXpath");
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
		}else {
			response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
		}

	}
}
