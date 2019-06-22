package api.sense;

import format.RespWrapper;
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
        else if("sense".equals(pathParam[0])&&"all".equals(pathParam[1])){
            String[] params1 = {"webId", "webName", "indexUrl"};
            String[][] websiteTable = DBUtil.select("website", params1);
            String[] params3 = {"id"};
            String[][]senseIds=DBUtil.select("sensestate", params3);
            for(int j=0;j<websiteTable.length;j++){
                boolean flag=false;
                String webId= websiteTable[j][0];
                for(int j2=0;j2<senseIds.length;j2++){
                    if(senseIds[j2][0].equals(webId)){
                        flag=true;
                    }

                }
                if(flag==false){
                    DBUtil.insert("sensestate",new String[]{"id","allLinks","trueLinks","status"},new String[]{webId,"0","0","stop"});


                }

            }
            String[] params2 = {"trueLinks","allLinks","status"};
            String[][]senseTable=DBUtil.select("sensestate", params2);

            int total = websiteTable.length+senseTable.length;
            if (senseTable.length>0) {
                for (int i = 0; i < websiteTable.length; i++) {
                    Map<String, Object> senseData = new HashMap<>();

                    /*add each pair of one row*/
                    for (int j = 0; j < params1.length; j++) {
                        senseData.put(params1[j], websiteTable[i][j]);
                    }
                    for (int j = 0; j < params2.length; j++) {
                        senseData.put(params2[j], senseTable[i][j]);
                    }
                    /*add one row*/
                    dataList.add(senseData);
                }
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(RespWrapper.build(dataList, total));

        }
        else if("sense".equals(pathParam[0])&&"show".equals(pathParam[1])){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            String getId=request.getParameter("getId");
            String getUrl = DBUtil.select("website",new String[]{"indexUrl"},new String[]{"webId"},new String[]{getId})[0][0];
            try {
                String[] p1 = {"homeUrl", "targetUrl"};
                String[] cond_params={"webId","homeUrl"};
                String[] cond_par_val={getId,getUrl};
                String[][] ans = DBUtil.select("sense", p1,cond_params,cond_par_val);
                if(getUrl.equals("all")){
                    ans = DBUtil.select("sense", p1);
                    for (int i = (ans.length-1); i >=0; i--) {
                        Map<String,Object> content=new HashMap<>();
                        content.put("homeUrl", ans[i][0]);
                        content.put("targetUrl", ans[i][1]);
                        dataList.add(content);
                    }
                }else {
                    for (int i = 0; i <ans.length; i++) {
                        Map<String,Object> content=new HashMap<>();
                        content.put("homeUrl", ans[i][0]);
                        content.put("targetUrl", ans[i][1]);
                        dataList.add(content);
                    }

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
