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


@WebServlet(name = "TemplateServlet",urlPatterns = {"/api/datacrawling/task/template/all"})
public class TemplateServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String [] template_params={"patternName","type"};//usable:true ,"formula","headerXPath"
    	String [] param={"webId","indexUrl"};
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
						content.put("url", res[i][1]);
						content.put("usable", "true");
    	    			content.put("templateName",template[j][0]);
        	    		content.put("templateType",template[j][1]);
        	    		dataList.add(content);
    	    		}
    	    	}
    	    	else{
    	    		Map<String,Object> content=new HashMap<>();
    	    		content.put("taskID", res[i][0]);
					content.put("url", res[i][1]);content.put("usable", "false");
					dataList.add(content);
    	    	}
    	    }
            response.getWriter().println(RespWrapper.build(dataList,dataList.size()));
    	}catch(Exception e){
            data.put("msg","模板参数修改失败");
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
    	}

    }
    
    
}
