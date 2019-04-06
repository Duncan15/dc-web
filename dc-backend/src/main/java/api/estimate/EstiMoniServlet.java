package api.estimate;

import services.esti.EstiThread;
import format.RespWrapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "EstiMoniServlet", urlPatterns = {"/api/datacrawling/estimoni"})
public class EstiMoniServlet extends HttpServlet {

    /*
    for api: /api/datacrawling/estimoni?action=status
    for api: /api/datacrawling/estimoni?action=option&option=start or stop&estiId=1
     */

    /*To store the threads that already start
     * in order to remove them when stop */
    private HashMap<String, EstiThread> threadMap = new HashMap<>();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String action = request.getParameter("action");
        Map<String, Object> data = new HashMap<>();
        if (action.equals("status")) {

        } else if (action.equals("option")) {

            String estiIdStr = request.getParameter("estiId");
            String option = request.getParameter("option");//option=start or stop;

            /*To check if the estiId is a real number*/

            if (option .equals("start")) {
                /*
                 * start a new thread.
                 * */
                if (threadMap.containsKey(estiIdStr)) {
                    if (threadMap.get(estiIdStr).getStatus().equals("start")) {
                        data.put("msg", "估测任务: " + estiIdStr + " 已经启动,请勿重复启动");
                    }else {
                        threadMap.remove(estiIdStr);
                        EstiThread newThread = new EstiThread();
                        newThread.setName(estiIdStr);
                        newThread.start();
                        threadMap.put(estiIdStr, newThread);
                        data.put("msg", "启动估测任务: " + estiIdStr + " 成功");
                    }
                } else {
                    try {
                        EstiThread newThread = new EstiThread();
                        newThread.setName(estiIdStr);
                        newThread.start();
                        threadMap.put(estiIdStr, newThread);
                        data.put("msg", "启动估测任务: " + estiIdStr + " 成功");
                    } catch (Exception e) {
                        threadMap.remove(estiIdStr);
                    }
                }
            } else if (option .equals("stop")) {
                /*
                 * stop a thread.*/
                if (!threadMap.containsKey(estiIdStr)) {
                    data.put("msg", "估测任务: " + estiIdStr + "尚未启动");
                } else {
                    threadMap.get(estiIdStr).interrupt();
                    threadMap.get(estiIdStr).setStatus("stop");
                    threadMap.remove(estiIdStr);
                    data.put("msg", "停止估测任务: " + estiIdStr);
                }

            } else {
                data.put("msg", "操作未定义,无法执行");
            }
            response.getWriter().println(RespWrapper.build(data));
        }
    }
}
