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
import java.util.*;


@WebServlet(name = "EstimateServlet", urlPatterns = {"/api/datacrawling/estimate/*"})
public class EstimateServlet extends HttpServlet {
    public static String projectPath;

    public static String setProjectPath(String pt) {
        projectPath = pt;
        return projectPath;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String[] pathParam = RequestParser.parsePath(request.getRequestURI(), 3);

        setProjectPath(request.getContextPath());
        /*
         *for estiamte/show/all
         * */
        if ("estimate".equals(pathParam[0]) && "show".equals(pathParam[1]) && "all".equals(pathParam[2])) {
            String[] params1 = {"webId", "webName", "indexUrl"};
            String[][] websiteTable = DBUtil.select("website", params1);
            String[] params2 = {"status", "rateBar", "result"};

            HashSet<String> estiIdSet = new HashSet<>();
            String[][] estiIdArray = DBUtil.select("estimate", new String[]{"estiId"});
            if (estiIdArray.length != 0) {
                for (int j = 0; j < estiIdArray.length; j++) {
                    estiIdSet.add(estiIdArray[j][0]);
                }
            }

            int total = websiteTable.length;

            List<Map<String, Object>> strings = new ArrayList<>();
            if (websiteTable.length > 0) {
                //下面处理每一行的数据。

                for (int i = 0; i < websiteTable.length; i++) {
                    Map<String, Object> estiData = new HashMap<>();
                    /*add each pair of one row*/
                    for (int j = 0; j < params1.length; j++) {
                        estiData.put(params1[j], websiteTable[i][j]);
                    }
                    /*check if this line of estiTable contains data
                     * line num is i*/

                    //websiteTable[i][0] is webId
                    if (!estiIdSet.contains(websiteTable[i][0])) {
                        DBUtil.insert("estimate", new String[]{"estiId"}, new String[]{websiteTable[i][0]});
                    }
                    String[][] estiLine = DBUtil.select("estimate", params2, new String[]{"estiId"}, new String[]{websiteTable[i][0]});
                    for (int j = 0; j < params2.length; j++) {

                        if (estiLine[0][j].equals("") || estiLine[0][j].isEmpty()) {
                            estiData.put(params2[j], "暂无");
                        } else {
                            estiData.put(params2[j], estiLine[0][j]);
                        }
                    }
                    if (estiData.get("status").equals("start")) {
                        estiData.replace("status", estiData.get("status"), "已开始");
                    }
                    if (estiData.get("status").equals("stop")) {
                        estiData.replace("status", estiData.get("status"), "已停止");
                    }
                    /*add one row*/
                    strings.add(estiData);
                }
            }

//            response.getWriter().println(RespWrapper.build(strings,strings.size()));
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

             * if in,we directly get.
             * */
            String[][] IDArray = DBUtil.select("estimate", new String[]{"estiId"});
            HashSet<String> IDset = new HashSet<>();
            for (String[] aID : IDArray) {
                IDset.add(aID[0]);
            }

            if (!IDset.contains(estiId)) {
                DBUtil.insert("estimate", new String[]{"estiId"}, new String[]{estiId});
            }


            Map<String, Object> data = new HashMap<>();
            String[] params = new String[]{
                    "linksXpath", "contentXpath", "startWord", "walkTimes", "contentLocation", "querySend"
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

            String[][] IDArray1 = DBUtil.select("urlbaseconf", new String[]{"webId"});
            HashSet<String> IDset1 = new HashSet<>();
            for (String[] aID : IDArray1) {
                IDset1.add(aID[0]);
            }
            String[] params1 = new String[]{
                    "prefix", "paramQuery", "paramPage", "startPageNum", "paramList", "paramValueList"
            };
            String[] conParams1 = {"webId"};
            String[] conPalues1 = {estiId};

            String[][] urlBaseConfData;
            if (IDset1.contains(estiId)) {
                urlBaseConfData = DBUtil.select("urlbaseconf", params1, conParams1, conPalues1);
            } else {
                urlBaseConfData = new String[1][params1.length];
                for (int i = 0; i < params1.length; i++) {
                    urlBaseConfData[0][i] = "";
                }
            }

            for (int i = 0; i < params1.length; i++) {
                if (urlBaseConfData[0][i] != null) {
                    data.put(params1[i], urlBaseConfData[0][i]);
                } else {
                    data.put(params1[i], "");
                }
            }


            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(RespWrapper.build(data));
        } else {
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
            String contentXpath = request.getParameter("contentXpath");
            String walkTimes = request.getParameter("walkTimes");
            String startWord = request.getParameter("startWord");
            String contentLocation = request.getParameter("contentLocation");
            String querySend = request.getParameter("querySend");

            String[] params = new String[]{
                    "linksXpath", "contentXpath", "walkTimes", "startWord", "contentLocation", "querySend"
            };
            String[] values = {linksXpath, contentXpath, walkTimes, startWord, contentLocation, querySend};
            String[] conParams = {"estiId"};
            String[] conValues = {estiId};
            boolean aBool = DBUtil.update("estimate", params, values, conParams, conValues);
            if (aBool) {
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
