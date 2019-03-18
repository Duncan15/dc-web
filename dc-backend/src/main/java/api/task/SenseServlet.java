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
        Map<String,Object> data=new HashMap<>();
        List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();
        if("sense".equals(pathParam[0])&&"new".equals(pathParam[1])){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            String url=request.getParameter("pageUrl");
            try {
                data.put("msg", "首页已经更改");
                response.getWriter().println(RespWrapper.build(data));
                WebCrawlerDemo webCrawlerDemo = new WebCrawlerDemo();
                webCrawlerDemo.setBaseLink1(url);
                webCrawlerDemo.myPrint(url);

            }catch(Exception e){
                System.out.println(e);
                data.put("msg","首页添加失败");
                response.getWriter().println(RespWrapper.build(data));
            }
        }
        else if("sense".equals(pathParam[0])&&"show".equals(pathParam[1])){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            try {
                String[] p1 = {"homeUrl", "targetUrl"};
                String[][] ans = DBUtil.select("sense", p1);
                for (int i = (ans.length-1); i >=0; i--) {
                    Map<String,Object> content=new HashMap<>();
                    content.put("homeUrl", ans[i][0]);
                    content.put("targetUrl", ans[i][1]);
                    dataList.add(content);
                }
                response.getWriter().println(RespWrapper.build(dataList,dataList.size()));
            }
            catch (Exception e){

                data.put("msg","模板参数获取失败");
                response.getWriter().println(RespWrapper.build(data));
            }



        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
