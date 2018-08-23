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
        String [] params={"webId","webName","indexUrl","runningMode","usable","createdTime","creator"};
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
            websitealone.put("URL",allwebsite2[i][2]);
            websitealone.put("runningMode",allwebsite2[i][3]);
            websitealone.put("usable",allwebsite2[i][4]);
            websitealone.put("createdTime",allwebsite2[i][5]);
            websitealone.put("creator",allwebsite2[i][6]);
           content.add(i,websitealone);
        }
        data.put("total",total);
        data.put("content",content);
        result.put("data",data);
        response.getWriter().write(result.toString());



    }
}
