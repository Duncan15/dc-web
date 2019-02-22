package api.task;

import enums.Driver;
import enums.RunningMode;
import enums.Usable;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * servlet focus on creating task
 */
@WebServlet(name = "TaskServlet",urlPatterns = {"/api/datacrawling/task/*"})
public class TaskServlet extends HttpServlet {
    /*
    for api: /api/datacrawling/task/new
     */
    /*
    for api: /api/datacrawling/task/urlparam/:id
             /api/datacrawling/task/loginparam/:id
             /api/datacrawling/task/downloadparam/:id
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String[] pathParam= RequestParser.parsePath(request.getRequestURI(),2);
        Map<String,Object> data=new HashMap<>();

        if("task".equals(pathParam[0])&&"new".equals(pathParam[1])){//for task/new
            //前端页面保证这些值均不为空，这里无需验证
            String taskName=request.getParameter("taskName");
            String runningMode=request.getParameter("runningMode");
            String workPath=request.getParameter("workPath");
            String driver=request.getParameter("driver");
            Driver d = Driver.ValueOf(driver);
            RunningMode r = RunningMode.ValueOf(runningMode);


            String taskID="";
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try {
                if(r == null) {
                    data.put("msg", "runningMode参数值错误");
                    response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
                } else if(d == null) {
                    data.put("msg", "driver参数值错误");
                    response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
                } else if (Config.checkTask(taskName)) {//if have existed the website in database
                    data.put("msg", "该任务名称已使用，请重新输入");
                    response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
                } else if (Config.setInterface(r, taskName, workPath, d)) {
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
        }else if("urlparam".equals(pathParam[0])) {//for task/urlparam/:id
            int taskID = 0;
            try{
                taskID=Integer.parseInt(pathParam[1]);
            }catch (NumberFormatException e){
                data.put("msg","id参数格式错误");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
                return;
            }
            int webId = taskID;
            String[] p= {"runningMode", "driver", "usable", "charset"};
            String[] ans = DBUtil.select("website", p, webId)[0];//here may happen nullPointerException
            RunningMode runningMode = RunningMode.valueOf(Integer.parseInt(ans[0]));
            Driver driver = Driver.valueOf(Integer.parseInt(ans[1]));
            Usable usable = Usable.valueOf(Integer.parseInt(ans[2]));

            //if url and downloadparam has been configured, it can be judged that the configuration is usable
            if (ans[3] != null && ans[3].length() != 0) {
                usable = Usable.have;
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            if(driver == Driver.none) {//driver is equal to none, it's unstructed or structed without driver
                String siteURL = request.getParameter("siteURL");
                String searchURL = request.getParameter("searchURL");
                String keywordName = request.getParameter("keywordName");
                String pageParamName = request.getParameter("pageParamName");
                String pageParamValue = request.getParameter("pageParamValue");
                String otherParamName = request.getParameter("otherParamName");
                String otherParamValue = request.getParameter("otherParamValue");

                String[]  param = {"indexUrl", "prefix", "paramQuery", "paramPage", "startPageNum", "paramList", "paramValueList", "usable"};
                String[] paramValue = {siteURL,searchURL,keywordName,pageParamName,pageParamValue,otherParamName,otherParamValue,usable.getValue() + ""};

                try{
                    if(DBUtil.update("website", param, paramValue, webId)) {
                        data.put("siteURL",siteURL);
                        data.put("searchURL",searchURL);
                        data.put("keywordName",keywordName);
                        data.put("pageParamName",pageParamName);
                        data.put("otherParamName",otherParamName);
                        data.put("pageParamValue",pageParamValue);
                        data.put("otherParamValue",otherParamValue);
                        if(runningMode == RunningMode.structed) {//it's stucted without driver
                            String paramQueryValueList=request.getParameter("paramQueryValueList");
                            String[] params = {"dataParamList"};
                            String[] paramsValue = {paramQueryValueList};
                            DBUtil.update("queryparam", params, paramsValue, webId);
                            data.put("paramQueryValueList",paramQueryValueList);
                        }
                        response.getWriter().println(RespWrapper.build(data));
                    } else {
                        data.put("msg","url参数修改失败");
                        response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
                    }

                }catch(Exception e){
                    response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
                }
            }
            else if(runningMode == RunningMode.structed){//it's structed with driver
                String iframeNav = request.getParameter("iframeNav").trim();
                String indexUrl = request.getParameter("siteURL").trim();
                String navValue = request.getParameter("navValue").trim();
                String iframeCon = request.getParameter("iframeCon").trim();
                String searchButton = request.getParameter("searchButton").trim();
                String resultRow = request.getParameter("resultRow".trim());
                String nextPageXPath = request.getParameter("nextPageXPath").trim();
                String pageNumXPath = request.getParameter("pageNumXPath").trim();
                String iframeSubParam = request.getParameter("iframeSubParam").trim();
                String arrow = request.getParameter("arrow").trim();
                String paramList = request.getParameter("otherParamName").trim();
                String paramValueList = request.getParameter("otherParamValue").trim();


                String[] params = {"webId","iframeNav","navValue","iframeCon","searchButton","resultRow","nextPageXPath"
                        ,"pageNumXPath","iframeSubParam","arrow"};
                String[] paramsValue = {webId+"",iframeNav,navValue,iframeCon,searchButton,resultRow,
                        nextPageXPath,pageNumXPath,iframeSubParam,arrow};

                String[] param = {"indexUrl", "paramList", "paramValueList", "usable"};
                String[] paramValue = {indexUrl, paramList, paramValueList, usable.getValue() + ""};
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
                        data.put("msg","url参数修改失败");
                        response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
                }
            }
        }else if("loginparam".equals(pathParam[0])) {//for task/loginparam/:id
            int taskID = 0;
            try{
                taskID = Integer.parseInt(pathParam[1]);
            }catch (NumberFormatException e){
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
                return;
            }
            int webId = taskID;
            String loginURL = request.getParameter("loginURL");
            String userNameXpath = request.getParameter("userNameID");
            String passwordXpath = request.getParameter("passwordID");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String submitXpath = request.getParameter("submitID");
            String[] p={ "runningMode","driver"};
            String[] ans = DBUtil.select("website", p,webId)[0];

            String[] param = {"userParam","pwdParam","userName","password","loginUrl", "submitXpath"};
            String[] paramValue = {userNameXpath, passwordXpath, username, password, loginURL, submitXpath};
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try{
                if(DBUtil.update("website", param, paramValue, webId)){
                    if(RunningMode.structed.name().equals(ans[0])&&(Driver.have.getValue() + "").equals(ans[1])) {//it's stucted with driver
                        String loginButton = request.getParameter("loginButton");
                        String[] params = {"loginButton"};
                        String[] paramsValue = {loginButton};
                        DBUtil.update("structedParam", params, paramsValue, webId);
                        data.put("loginButton",loginButton);
                    }
                    data.put("loginURL",loginURL);
                    data.put("userNameID",userNameXpath);
                    data.put("passwordID",passwordXpath);
                    data.put("username",username);
                    data.put("password",password);
                    response.getWriter().println(RespWrapper.build(data));
                }
                else {
                    data.put("msg","login参数修改失败");
                    response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
                }
            }catch(Exception e){
                data.put("msg","login参数修改失败");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
            }
        }else if("downloadparam".equals(pathParam[0])){//for task/downloadparam/:id
            int taskID = 0;
            try{
                taskID=Integer.parseInt(pathParam[1]);
            }catch (NumberFormatException e){
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
                return;
            }
            int webId = taskID;
            String threadNum = request.getParameter("threadNum");
            String timeout = request.getParameter("timeout");
            String charset = request.getParameter("charset");
            String datagross = request.getParameter("datagross");
            Usable usable = Usable.none;

            //if url and downloadparam has been configured, it can be judged that the configuration is usable
            String[] p={"indexUrl"};
            String ans =DBUtil.select("website", p,webId)[0][0];
            if(ans!=null ||ans.length()!=0)
				usable = Usable.have;

            String[] param = {"threadNum", "timeout", "charset", "databaseSize", "usable"};
            String[] paramValue = {threadNum, timeout, charset, datagross, usable.getValue() + ""};


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
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
        }

    }

	/*
    for api: /api/datacrawling/task/:id
    for api: /api/datacrawling/task/all
          
     */
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
         String[] pathParam=RequestParser.parsePath(request.getRequestURI(),2);
         Map<String,Object> data=new HashMap<>();
         if("task".equals(pathParam[0]) && "all".equals(pathParam[1])){//for task/all
             String [] params={"webId","webName","runningMode","workFile","driver","createtime","usable",
                     "indexUrl","prefix","paramQuery","paramPage","startPageNum","paramList","paramValueList","userParam",
                     "pwdParam","username","password","loginURL","threadNum","timeout","charset","databaseSize","creator","submitXpath"};
             List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();
             response.setContentType("application/json");
             response.setCharacterEncoding("UTF-8");
             try {
                 String[][] websites = DBUtil.select("website", params);
                 int total = websites.length;
                 for (int i = 0; i < total; i++) {
                     Map<String, Object> website = new HashMap<>();

                     website.put("taskID", websites[i][0]);
                     website.put("taskName", websites[i][1]);
                     website.put("runningMode", websites[i][2]);
                     website.put("workPath", websites[i][3]);
                     website.put("driver", Driver.valueOf(Integer.parseInt(websites[i][4])));
                     website.put("createtime", websites[i][5]);
                     website.put("usable", Usable.valueOf(Integer.parseInt(websites[i][6])));
                     website.put("siteURL", websites[i][7]);
                     website.put("searchURL", websites[i][8]);
                     website.put("keywordName", websites[i][9]);
                     website.put("pageParamName", websites[i][10]);
                     website.put("pageParamValue", websites[i][11]);
                     website.put("otherParamName", websites[i][12]);
                     website.put("otherParamValue", websites[i][13]);
                     website.put("userNameID", websites[i][14]);
                     website.put("passwordID", websites[i][15]);
                     website.put("username", websites[i][16]);
                     website.put("password", websites[i][17]);
                     website.put("loginURL", websites[i][18]);
                     website.put("threadNum", websites[i][19]);
                     website.put("timeout", websites[i][20]);
                     website.put("charset", websites[i][21]);
                     website.put("datagross", websites[i][22]);
                     website.put("creator", websites[i][23]);
                     website.put("submitXpath", websites[i][24]);
                     dataList.add(website);
                 }
                 response.getWriter().println(RespWrapper.build(dataList,dataList.size()));
             }catch(Exception e){
                 data.put("msg","获取信息失败");
                 response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
             }
         } else if("task".equals(pathParam[0])){//for task/:id
             int taskID = 0;
             try{
                 taskID=Integer.parseInt(pathParam[1]);
             }catch (NumberFormatException e){
                 response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
                 return;
             }
             int webId =taskID;
             response.setContentType("application/json");
             response.setCharacterEncoding("UTF-8");
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
                if(RunningMode.structed.name().equals(taskData[2]) && (Driver.have + "").equals(taskData[4])){//if structed with driver
						String[] params = {"iframeNav","navValue","iframeCon","searchButton","resultRow","nextPageXPath"
                        ,"pageNumXPath","iframeSubParam","arrow","loginButton"};
						String[] structedData= DBUtil.select("structedParam",params,webId)[0];
                    for(int i=0;i<params.length;i++)
                        data.put(params[i], structedData[i]);
                }
                else if(RunningMode.structed.name().equals(taskData[2]) && (Driver.none + "").equals(taskData[4])){
                    String[] params = {"dataParamList"};
                    data.put("paramQueryValueList", DBUtil.select("queryparam", params, webId)[0][0]);
                }
                response.getWriter().println(RespWrapper.build(data));
             }catch(Exception e){
                 data.put("msg","任务参数获取失败");
                 response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
             }
         }else {
             response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
         }


    }
}
