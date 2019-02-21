package api.task;

import format.RespWrapper;
import util.Config;
import util.DBUtil;
import util.RequestParser;
import util.WebCrawlerDemo;
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


@WebServlet(name = "SenseServlet",urlPatterns = {"/api/datacrawling/sense/*"})
public class SenseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String[] pathParam= RequestParser.parsePath(request.getRequestURI(),2);
        if("sense".equals(pathParam[0])&&"new".equals(pathParam[1])){
            String url=request.getParameter("pageUrl");
            //
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String,Object> data=new HashMap<>();
            try {
                WebCrawlerDemo webCrawlerDemo = new WebCrawlerDemo();
                webCrawlerDemo.myPrint(url);
                    data.put("msg", "首页已经更改");
                    response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, data));


            }catch(Exception e){
                data.put("msg","首页添加失败");
                response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
            }
            //


        }
        else if("sense".equals(pathParam[0])&&"show".equals(pathParam[1])){
            String[]p1={"homeUrl","targetUrl"};
            String [][] ans=DBUtil.select("sense",p1);
            Map<String,Object> data=new HashMap<>();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            for(int i=0;i<ans.length;i++){
                data.put("homeUrl",ans[i][0]);
                data.put("targetUrl",ans[i][1]);

            }
            response.getWriter().println(RespWrapper.build(data));
        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
