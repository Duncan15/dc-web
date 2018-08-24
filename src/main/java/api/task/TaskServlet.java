package api.task;

import format.RespWrapper;
import util.Config;
import util.DBUtil;
import util.RequestParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "TaskServlet",urlPatterns = {"/api/datacrawling/task/*"})
public class TaskServlet extends HttpServlet {
    /*
    for api: /api/datacrawling/task/new
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String[] pathParam= RequestParser.parsePath(request.getRequestURI(),2);
        if("task".equals(pathParam[0])&&"new".equals(pathParam[1])){
            //前端页面保证这些值均不为空，这里无需验证
            String taskName=request.getParameter("taskName");
            String runningMode=request.getParameter("runningMode");
            String workPath=request.getParameter("workPath");
            String driver=request.getParameter("driver");

            String taskID="";
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String,Object> data=new HashMap<>();
            try {
                if (Config.checkTask(taskName)) {//if have existed the website in database
                    data.put("msg", "该任务名称已使用，请重新输入");
                    response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
                } else if (Config.setInterface(runningMode, taskName, workPath, driver)) {
                    taskID = DBUtil.getLastWebId() + "";
                    data.put("taskID", taskID);
                    data.put("taskName", taskName);
                    data.put("runningMode", runningMode);
                    data.put("workPath", workPath);

                    response.getWriter().println(RespWrapper.build(data));
                }

            }catch(Exception e){
                data.put("msg","任务添加失败");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
            }
        }else {
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
        }

    }
    /*
    for api: /api/datacrawling/task/urlparam/:id
             /api/datacrawling/task/loginparam/:id
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String[] pathParam=RequestParser.parsePath(request.getRequestURI(),2);
        if("urlparam".equals(pathParam[0])){
            int taskID=0;
            try{
                taskID=Integer.parseInt(pathParam[1]);
            }catch (NumberFormatException e){
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
                return;
            }
            Map<String,Object> data=new HashMap<>();
            int webId =taskID;
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
        }else if("loginparam".equals(pathParam[0])){
            int taskID=0;
            try{
                taskID=Integer.parseInt(pathParam[1]);
            }catch (NumberFormatException e){
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
                return;
            }
            Map<String,Object> data=new HashMap<>();
            int webId =taskID;
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
        }else if("downloadparam".equals(pathParam[0])){
            int taskID=0;
            try{
                taskID=Integer.parseInt(pathParam[1]);
            }catch (NumberFormatException e){
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
                return;
            }
            Map<String,Object> data=new HashMap<>();
            int webId =taskID;
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

        }else {
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
        }
    }
	/*
    for api: /api/datacrawling/task/:id
          
     */
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> data=new HashMap<>();
        String url=request.getRequestURL().toString();
        int webId =Integer.parseInt(url.substring(url.lastIndexOf("/")+1));
        try{
            String[]  param = {"webId","webName","runningMode","workFile","driver","createtime","usable",
                    "indexUrl","prefix","paramQuery","paramPage","startPageNum","paramList","paramValueList",
                    "userParam","pwdParam","userName","password","loginUrl","threadNum","timeout","charset","databaseSize"};
           String[] taskData= DBUtil.select("website",param,webId)[0];
            String[]  keys = {"taskID","taskName","runningMode","workPath","driver","createtime","usable",
                    "siteURL","searchURL","keywordName","pageParamName","pageParamValue","otherParamName","otherParamValue",
                    "userNameID","passwordID","username","password","loginURL","threadNum","timeout","charset","datagross"};

            for(int i=0;i<keys.length;i++)
                data.put(keys[i], taskData[i]);
            System.out.println(taskData[2]);
            if("structed".equals(taskData[2])){
                String[] params = {"iframeNav","navValue","iframeCon","searchButton","resultRow","nextPageXPath"
                        ,"pageNumXPath","iframeSubParam","arrow"};
                String[] structedData= DBUtil.select("structedParam",params,webId)[0];
                for(int i=0;i<params.length;i++)
                    data.put(params[i], structedData[i]);
            }
            response.getWriter().println(RespWrapper.build(data));
        }catch(Exception e){
            data.put("msg","任务参数获取失败");
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
        }


    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
