package api.task;

import format.RespWrapper;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "TaskDataServlet",urlPatterns = {"/api/datacrawling/task/*"})
public class TaskDataServlet extends HttpServlet {
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
}
