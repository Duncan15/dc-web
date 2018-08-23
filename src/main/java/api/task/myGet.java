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
import java.util.List;

@WebServlet(name = "myGet", urlPatterns = {"/api/datacrawling/request/all"})
public class myGet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] params = {"requestID", "requestName", "requestDesc","createdTime"};
        String[][] requesttable = DBUtil.select("requesttable", params);
        int total = requesttable.length;
        List<String[]> strings = new ArrayList<>();
        for (int i = 0; i < requesttable.length; i++) {
            int strLen = requesttable[0].length;
            String[] tempStr = new String[strLen];
            for (int j = 0; j < requesttable[0].length; j++) {
                tempStr[j] = params[j] + ":" + requesttable[i][j];
            }
            strings.add(tempStr);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(RespWrapper.build(strings, total));

    }

}
