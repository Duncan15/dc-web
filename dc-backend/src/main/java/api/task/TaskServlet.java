package api.task;

import enums.Base;
import enums.Driver;
import enums.RunningMode;
import enums.Usable;
import format.RespWrapper;
import services.ConfigService;
import util.Initializer;
import util.DBUtil;
import util.RequestParser;
import util.Verifier;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] pathParam = RequestParser.parsePath(request.getRequestURI(), 2);
        Map<String,Object> data = new HashMap<>();

        if("task".equals(pathParam[0]) && "new".equals(pathParam[1])){//for task/new
            String taskName = request.getParameter("taskName");
            String runningMode = request.getParameter("runningMode");
            String workPath = request.getParameter("workPath");
            String driver = request.getParameter("driver");
            String base = request.getParameter("base");
            String siteURL = request.getParameter("siteURL");
            Driver d = Driver.ValueOf(driver);
            RunningMode r = RunningMode.ValueOf(runningMode);
            Base b = Base.ValueOf(base);


            String taskID = "";
            if(workPath.contains("/") || workPath.contains("\\")) {
                data.put("msg", "工作路径后缀不包含斜杠或反斜杠");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
                return;
            }
            workPath = Paths.get(ConfigService.getBaseWorkDir(), workPath).toString();
            if(r == null) {
                data.put("msg", "runningMode参数值错误");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
            } else if (d == null) {
                data.put("msg", "driver参数值错误");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
            } else if (b == null) {
                data.put("msg", "base参数值错误");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
            } else if (Verifier.varifyTaskName(taskName)) {//if have existed the website in database
                data.put("msg", "该任务名称已使用，请重新输入");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
            } else if (Initializer.init(r, d, b, taskName, workPath, siteURL)) {
                taskID = DBUtil.getLastWebId() + "";
                data.put("taskID", taskID);
                data.put("taskName", taskName);
                data.put("runningMode", runningMode);
                data.put("workPath", workPath);
                data.put("base", base);
                data.put("siteURL", siteURL);
                response.getWriter().println(RespWrapper.build(data));
            }
        } else {
            Integer webId = 0;
            if((webId = Verifier.verifyInt(pathParam[1])) == null) {
                data.put("msg","id参数格式错误");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
                return;
            }

            if("urlparam".equals(pathParam[0])) {//for task/urlparam/:id

                String[] p= {"runningMode", "driver", "usable", "base"};
                String[] ans = DBUtil.select("website", p, webId)[0];//here may happen nullPointerException
                RunningMode runningMode = RunningMode.ValueOf(ans[0]);
                Driver driver = Driver.valueOf(Integer.parseInt(ans[1]));
                Usable usable = Usable.valueOf(Integer.parseInt(ans[2]));
                Base base = Base.valueOf(Integer.parseInt(ans[3]));
                if (Verifier.verifyUsable(webId)) {
                    usable = Usable.have;
                }
                if (RunningMode.unstructed == runningMode) {
                    if (Base.urlBased == base) {
                        String searchURL = request.getParameter("searchURL");
                        String keywordName = request.getParameter("keywordName");
                        String pageParamName = request.getParameter("pageParamName");
                        String pageParamValue = request.getParameter("pageParamValue");
                        String otherParamName = request.getParameter("otherParamName");
                        String otherParamValue = request.getParameter("otherParamValue");

                        String[]  param = {"prefix", "paramQuery", "paramPage", "startPageNum", "paramList", "paramValueList"};
                        String[] paramValue = {searchURL,keywordName,pageParamName,pageParamValue,otherParamName,otherParamValue};

                        //check whether urlBaseConf exists or not
                        if (!Verifier.verifyExist(webId, "urlBaseConf")) {
                            DBUtil.insert("urlBaseConf", new String[]{"webId"}, new String[]{"" + webId});
                        }

                        if(DBUtil.update("urlBaseConf", param, paramValue, webId)) {
                            DBUtil.update("website", new String[]{"usable"}, new String[]{"" + usable.getValue()}, webId);
                            data.put("searchURL",searchURL);
                            data.put("keywordName",keywordName);
                            data.put("pageParamName",pageParamName);
                            data.put("otherParamName",otherParamName);
                            data.put("pageParamValue",pageParamValue);
                            data.put("otherParamValue",otherParamValue);
                            response.getWriter().println(RespWrapper.build(data));
                        } else {
                            data.put("msg","url参数修改失败");
                            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
                        }
                    } else if (Base.apiBased == base) {
                        //TODO

                        //check whether apiBaseConf exists or not
                        if (!Verifier.verifyExist(webId, "apiBaseConf")) {
                            DBUtil.insert("apiBaseConf", new String[]{"webId"}, new String[]{"" + webId});
                        }

                    }
                } else if (RunningMode.structed == runningMode) {
                    if (Driver.have == driver) {
                        //TODO

                        String iframeNav = request.getParameter("iframeNav").trim();
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

                        String[] param = {"paramList", "paramValueList", "usable"};
                        String[] paramValue = {paramList, paramValueList, usable.getValue() + ""};

                        String[] params = {"webId","iframeNav","navValue","iframeCon","searchButton","resultRow","nextPageXPath"
                                ,"pageNumXPath","iframeSubParam","arrow"};
                        String[] paramsValue = {webId+"",iframeNav,navValue,iframeCon,searchButton,resultRow,
                                nextPageXPath,pageNumXPath,iframeSubParam,arrow};

                        if(DBUtil.update("website", param, paramValue, webId) && DBUtil.update("structedParam", params, paramsValue,webId)){
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
                    } else if (Driver.none == driver) {
                        //TODO



                        String paramQueryValueList = request.getParameter("paramQueryValueList");
                        String[] params = {"dataParamList"};
                        String[] paramsValue = {paramQueryValueList};
                        DBUtil.update("queryparam", params, paramsValue, webId);
                        data.put("paramQueryValueList",paramQueryValueList);
                    }
                }

            } else if("loginparam".equals(pathParam[0])) {//for task/loginparam/:id

                String loginURL = request.getParameter("loginURL");
                String userNameXpath = request.getParameter("userNameID");
                String passwordXpath = request.getParameter("passwordID");
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String submitXpath = request.getParameter("submitXpath");

                String[] param = {"userNameXpath","passwordXpath","userName","password","loginUrl", "submitXpath"};
                String[] paramValue = {userNameXpath, passwordXpath, username, password, loginURL, submitXpath};
                if (!Verifier.verifyExist(webId, "extraConf")) {
                    DBUtil.insert("extraConf", new String[]{"webId"}, new String[]{"" + webId});
                }

                if(DBUtil.update("extraConf", param, paramValue, webId)){

                    data.put("loginURL",loginURL);
                    data.put("userNameID",userNameXpath);
                    data.put("passwordID",passwordXpath);
                    data.put("username",username);
                    data.put("password",password);
                    data.put("submitXpath", submitXpath);
                    response.getWriter().println(RespWrapper.build(data));
                }
                else {
                    data.put("msg","login参数修改失败");
                    response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
                }
            }else if("downloadparam".equals(pathParam[0])){//for task/downloadparam/:id
                String threadNum = request.getParameter("threadNum");
                String timeout = request.getParameter("timeout");
                String charset = request.getParameter("charset");
                String datagross = request.getParameter("datagross");

                String[] param = {"threadNum", "timeout", "charset", "databaseSize"};
                String[] paramValue = {threadNum, timeout, charset, datagross};
                if (!Verifier.verifyExist(webId, "extraConf")) {
                    DBUtil.insert("extraConf", new String[]{"webId"}, new String[]{"" + webId});
                }
                if(DBUtil.update("extraConf", param, paramValue, webId)){
                    data.put("threadNum",threadNum);
                    data.put("timeout",timeout);
                    data.put("charset",charset);
                    data.put("datagross",datagross);

                    Usable usable = Usable.none;
                    if(Verifier.verifyUsable(webId))
                        usable = Usable.have;
                    DBUtil.update("website", new String[]{"usable"}, new String[]{"" + usable.getValue()}, webId);

                    response.getWriter().println(RespWrapper.build(data));
                } else {
                    data.put("msg","下载参数修改失败");
                    response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
                }

            }else {
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
            }

        }

    }

	/*
    for api: /api/datacrawling/task/:id
    for api: /api/datacrawling/task/all
          
     */
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         String[] pathParam=RequestParser.parsePath(request.getRequestURI(),2);
         Map<String,Object> data=new HashMap<>();
         if("task".equals(pathParam[0]) && "all".equals(pathParam[1])){//for task/all
             String [] params={"webId", "webName", "runningMode", "workFile", "driver", "createtime", "usable",
                     "indexUrl", "creator", "base"};
             //"prefix", "paramQuery", "paramPage", "startPageNum", "paramList", "paramValueList"
             List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();
             String[][] websites = DBUtil.select("website", params);
             int total = websites.length;
             for (int i = 0; i < total; i++) {
                 Map<String, Object> website = new HashMap<>();
                 website.put("taskID", websites[i][0]);
                 website.put("taskName", websites[i][1]);
                 website.put("runningMode", websites[i][2]);
                 website.put("workPath", websites[i][3]);

                 website.put("createtime", websites[i][5]);
                 website.put("usable", Usable.valueOf(Integer.parseInt(websites[i][6])));
                 website.put("siteURL", websites[i][7]);
//                 website.put("searchURL", websites[i][8]);
//                 website.put("keywordName", websites[i][9]);
//                 website.put("pageParamName", websites[i][10]);
//                 website.put("pageParamValue", websites[i][11]);
//                 website.put("otherParamName", websites[i][12]);
//                 website.put("otherParamValue", websites[i][13]);
                 website.put("creator", websites[i][8]);
                 RunningMode r = RunningMode.ValueOf(websites[i][2]);
                 if (r == RunningMode.unstructed) {
                     website.put("driver", "未知");
                     website.put("base", Base.valueOf(Integer.parseInt(websites[i][9])));
                 } else {
                     website.put("driver", Driver.valueOf(Integer.parseInt(websites[i][4])));
                     website.put("base", "未知");
                 }

                 dataList.add(website);
             }
             response.getWriter().println(RespWrapper.build(dataList,dataList.size()));
         } else if ("task".equals(pathParam[0])) {//for task/:id
             Integer webId = 0;
             if ((webId = Verifier.verifyInt(pathParam[1])) == null) {
                 data.put("msg", "id参数格式错误");
                 response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));
                 return;
             }

             String[]  websiteParam = {"webId", "webName", "runningMode", "workFile", "driver", "createtime", "usable", "indexUrl", "base"};
             String[] extraParam = {"userNameXpath", "passwordXpath", "userName", "password", "loginUrl", "threadNum",
                     "timeout", "charset", "databaseSize", "submitXpath"};
             String[]  keys = {"taskID", "taskName", "runningMode", "workPath", "driver", "createtime", "usable", "siteURL", "base",
                     "userNameID","passwordID","username","password","loginURL","threadNum","timeout","charset","datagross", "submitXpath"};

             int step = 0;
             String[] taskData = DBUtil.select("website", websiteParam, webId)[0];

             RunningMode r = RunningMode.ValueOf(taskData[2]);
             Driver v = Driver.valueOf(Integer.parseInt(taskData[4]));
             Base b = Base.valueOf(Integer.parseInt(taskData[8]));

             for(int i = 0; i < taskData.length; step++, i++)
                 data.put(keys[step], taskData[i]);
             if (Verifier.verifyExist(webId, "extraConf")) {
                 taskData = DBUtil.select("extraConf", extraParam, webId)[0];
                 for (int i = 0; i < taskData.length; step++, i++) {
                     data.put(keys[step], taskData[i]);
                 }
             } else {
                 for (; step < keys.length; step++) {
                     data.put(keys[step], "");
                 }
             }

             if (RunningMode.unstructed == r) {
                 if (Base.urlBased == b) {
                     String[] params = {"prefix","paramQuery","paramPage","startPageNum","paramList","paramValueList"};
                     String[] urlBasedData = DBUtil.select("urlBaseConf", params, webId)[0];
                     String[] ansKeys = {"searchURL","keywordName","pageParamName","pageParamValue","otherParamName","otherParamValue"};
                     for (int i = 0; i < params.length; i++) {
                         data.put(ansKeys[i], urlBasedData[i]);
                     }
                 } else if(Base.apiBased == b) {
                     //TODO:
                 }
             } else if (RunningMode.structed == r) {
                 if (Driver.have == v) {
                     String[] params = {"iframeNav","navValue","iframeCon","searchButton","resultRow","nextPageXPath"
                             ,"pageNumXPath","iframeSubParam","arrow","loginButton"};
                     String[] structedData = DBUtil.select("structedParam",params,webId)[0];
                     for(int i=0;i<params.length;i++)
                         data.put(params[i], structedData[i]);
                 } else if (Driver.none == v) {
                     String[] params = {"dataParamList"};
                     data.put("paramQueryValueList", DBUtil.select("queryparam", params, webId)[0][0]);
                 }
             }
             response.getWriter().println(RespWrapper.build(data));
         }else {
             response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,null));
         }


    }
}
