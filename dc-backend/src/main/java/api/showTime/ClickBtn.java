package api.showTime;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

@WebServlet(name = "ClickBtn",urlPatterns = {"/api/ClickBtn"})
public class ClickBtn extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Date data = new Date();
        System.out.println(data);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print("成功记录时间");
    }
}
