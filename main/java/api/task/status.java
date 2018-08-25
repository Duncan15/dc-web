package api.task;

import lpjava.findstatus;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@WebServlet(name = "status",urlPatterns = {"/api/datacrawling/task/status"})
public class status extends HttpServlet {

    //传入进程名称processName

    private static boolean findProcess(String processName) {
        BufferedReader bufferedReader = null;
        try {
            Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + processName +'"');
            bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(processName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ex) {}
            }
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String webId=request.getParameter("id");
    String taskstatus="stoped";
    JSONObject resultans = new JSONObject();
    JSONObject data=new JSONObject();
    if(findProcess(webId)==true){
        taskstatus="started";
    }
    data.put("taskID",webId);
    data.put("taskStatus",taskstatus);
    resultans.put("errno",0);
    resultans.put("data",data);
    response.getWriter().write(resultans.toString());
    }
}
