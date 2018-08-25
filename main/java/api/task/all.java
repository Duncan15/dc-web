package api.task;
import util.DBUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "all",urlPatterns = {"/api/datacrawling/task/all"})
public class all extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int errno=0;
        String [] params={"webId","webName","runningMode","workFile","driver","createtime","usable",
                "indexUrl","prefix","paramQuery","paramPage","startPageNum","paramList","paramValueList","userParam",
                "pwdParam","username","password","loginURL","threadNum","timeout","charset","databaseSize","creator"};
        try {
            String[][] allwebiste1 = DBUtil.select("website", params);
        }
        catch (Exception e)
        {
            errno=500;
        }
        String[][]allwebsite2=DBUtil.select("website", params);
        int total=allwebsite2.length;
        JSONObject result = new JSONObject();
        result.put("errno",errno);
        JSONObject data = new JSONObject();
        JSONArray content = new JSONArray();
        for(int i=0;i<total;i++){

//            String websitealone="website"+String.valueOf(i);
            JSONObject websitealone = new JSONObject();
            websitealone.put("taskID",allwebsite2[i][0]);
            websitealone.put("taskName",allwebsite2[i][1]);
            websitealone.put("runningMode",allwebsite2[i][2]);
            websitealone.put("workPath",allwebsite2[i][3]);
            websitealone.put("driver",allwebsite2[i][4]);
            websitealone.put("createtime",allwebsite2[i][5]);
            websitealone.put("usable",allwebsite2[i][6]);
            websitealone.put("siteURL",allwebsite2[i][7]);
            websitealone.put("searchURL",allwebsite2[i][8]);
            websitealone.put("keywordName",allwebsite2[i][9]);
            websitealone.put("pageParamName",allwebsite2[i][10]);
            websitealone.put("pageParamValue",allwebsite2[i][11]);
            websitealone.put("otherParamName",allwebsite2[i][12]);
            websitealone.put("otherParamValue",allwebsite2[i][13]);
            websitealone.put("userNameID",allwebsite2[i][14]);
            websitealone.put("passwordID",allwebsite2[i][15]);
            websitealone.put("username",allwebsite2[i][16]);
            websitealone.put("password",allwebsite2[i][17]);
            websitealone.put("loginURL",allwebsite2[i][18]);
            websitealone.put("threadNum",allwebsite2[i][19]);
            websitealone.put("timeout",allwebsite2[i][20]);
            websitealone.put("charset",allwebsite2[i][21]);
            websitealone.put("datagross",allwebsite2[i][22]);
            websitealone.put("creator",allwebsite2[i][23]);
           content.add(i,websitealone);
        }
        data.put("total",total);
        data.put("content",content);
        result.put("data",data);
        response.getWriter().write(result.toString());



    }
}
