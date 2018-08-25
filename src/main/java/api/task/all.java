package api.task;
import format.RespWrapper;
import util.DBUtil;
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


@WebServlet(name = "all",urlPatterns = {"/api/datacrawling/task/all"})
public class all extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String [] params={"webId","webName","runningMode","workFile","driver","createtime","usable",
                "indexUrl","prefix","paramQuery","paramPage","startPageNum","paramList","paramValueList","userParam",
                "pwdParam","username","password","loginURL","threadNum","timeout","charset","databaseSize","creator"};
        List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();
        Map<String,Object> data=new HashMap<>();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String[][] allwebsite2 = DBUtil.select("website", params);
            int total = allwebsite2.length;

            for (int i = 0; i < total; i++) {
                Map<String, Object> websitealone = new HashMap<>();

                websitealone.put("taskID", allwebsite2[i][0]);
                websitealone.put("taskName", allwebsite2[i][1]);
                websitealone.put("runningMode", allwebsite2[i][2]);
                websitealone.put("workPath", allwebsite2[i][3]);
                websitealone.put("driver", allwebsite2[i][4]);
                websitealone.put("createtime", allwebsite2[i][5]);
                websitealone.put("usable", allwebsite2[i][6]);
                websitealone.put("siteURL", allwebsite2[i][7]);
                websitealone.put("searchURL", allwebsite2[i][8]);
                websitealone.put("keywordName", allwebsite2[i][9]);
                websitealone.put("pageParamName", allwebsite2[i][10]);
                websitealone.put("pageParamValue", allwebsite2[i][11]);
                websitealone.put("otherParamName", allwebsite2[i][12]);
                websitealone.put("otherParamValue", allwebsite2[i][13]);
                websitealone.put("userNameID", allwebsite2[i][14]);
                websitealone.put("passwordID", allwebsite2[i][15]);
                websitealone.put("username", allwebsite2[i][16]);
                websitealone.put("password", allwebsite2[i][17]);
                websitealone.put("loginURL", allwebsite2[i][18]);
                websitealone.put("threadNum", allwebsite2[i][19]);
                websitealone.put("timeout", allwebsite2[i][20]);
                websitealone.put("charset", allwebsite2[i][21]);
                websitealone.put("datagross", allwebsite2[i][22]);
                websitealone.put("creator", allwebsite2[i][23]);
                dataList.add(websitealone);
            }
            response.getWriter().println(RespWrapper.build(dataList,dataList.size()));
        }catch(Exception e){
            data.put("msg","获取信息失败");
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,data));
        }

        }


    }
