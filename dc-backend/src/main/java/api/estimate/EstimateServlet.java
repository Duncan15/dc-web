package api.estimate;

import format.RespWrapper;
import util.DBUtil;
import util.RequestParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@WebServlet(name = "EstimateServlet", urlPatterns = {"/api/datacrawling/estimate/*"})
public class EstimateServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String[] pathParam = RequestParser.parsePath(request.getRequestURI(), 3);

        /*
         *for estiamte/show/all
         * */
        if ("estimate".equals(pathParam[0]) && "show".equals(pathParam[1]) && "all".equals(pathParam[2])) {
            String[] params1 = {"webId", "webName", "indexUrl", "createTime"};
            String[][] websiteTable = DBUtil.select("website", params1);

            String[] params2 = {"status", "rateBar","result"};
            String[][]estiTable=DBUtil.select("estimate", params2);

            int total = websiteTable.length+estiTable.length;

            List<Map<String, Object>> strings = new ArrayList<>();
            for (int i=0;i<websiteTable.length;i++) {
                Map<String, Object> estiData = new HashMap<>();

                /*add each pair of one row*/
                for (int j = 0; j < params1.length; j++) {
                    estiData.put(params1[j], websiteTable[i][j]);
                }
                for (int j = 0; j < params2.length; j++) {
                    estiData.put(params2[j], estiTable[i][j]);
                }

                /*add one row*/
                strings.add(estiData);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(RespWrapper.build(strings, total));


        } else if ("estimate".equals(pathParam[0]) && "change".equals(pathParam[1])) {

            //for estimate/change/estiId

            /*In use when click change button.
             * */

            String estiId = pathParam[2];

            /*
            * check whether ID is in the estimate table;
            * if not,we create one.Then update.
            * if in,we directly update.
            * */
            String[][] IDArray = DBUtil.select("estimate", new String[]{"estiId"});
            HashSet<String>IDset=new HashSet<>();
            for(String[] aID:IDArray){
                IDset.add(aID[0]);
            }

            if (!IDset.contains(estiId)){
                DBUtil.insert("estimate", new String[]{"estiId"}, new String[]{estiId});
            }

            Map<String, Object> data = new HashMap<>();
            String[] params = new String[]{
                    "linksXpath", "pagesInfoId", "contentXpath", "startWord","walkTimes"
            };
            String[] conParams = {"estiId"};
            String[] conPalues = {estiId};
            String[][] estiData = DBUtil.select("estimate", params, conParams, conPalues);
            for (int i = 0; i < params.length; i++) {
                if (estiData[0][i] != null) {
                    data.put(params[i], estiData[0][i]);
                } else {
                    data.put(params[i], "");
                }
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(RespWrapper.build(data));
        }
            else
        {
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, null));
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String[] pathParam = RequestParser.parsePath(request.getRequestURI(), 3);

        if ("estimate".equals(pathParam[0]) && "update".equals(pathParam[1])) {
            /*
            for api/datacrawling/estimate/update/3?
            *  */
            String estiId = pathParam[2];

            /*Next, get all the 5 params and write them into estimate table*/
            String linksXpath = request.getParameter("linksXpath");
            String pagesInfoId = request.getParameter("pagesInfoId");
            String contentXpath = request.getParameter("contentXpath");
            String walkTimes = request.getParameter("walkTimes");
            String startWord = request.getParameter("startWord");


            String[] params = new String[]{
                    "linksXpath", "pagesInfoId", "contentXpath", "walkTimes","startWord"
            };
            String []values={linksXpath,pagesInfoId,contentXpath,walkTimes,startWord};
            String[] conParams = {"estiId"};
            String[] conValues = {estiId};
            boolean aBool=DBUtil.update("estimate",params,values,conParams,conValues);
            if (aBool){
                System.out.println("The conf is updated.");
            }

            Map<String, Object> data = new HashMap<>();
            data.put("estiID", estiId);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(RespWrapper.build(data));
        } else {
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR, null));
        }


    }

}
