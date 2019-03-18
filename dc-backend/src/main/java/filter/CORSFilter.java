package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CORSFilter",urlPatterns = {"/api/*"})
public class CORSFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String origin=((HttpServletRequest)req).getHeader("Origin");
        HttpServletResponse httpServletResponse=(HttpServletResponse)resp;
        httpServletResponse.addHeader("Access-Control-Allow-Origin",origin);
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "Content-Type");
        httpServletResponse.addHeader("Access-Control-Max-Age", "1800");
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
