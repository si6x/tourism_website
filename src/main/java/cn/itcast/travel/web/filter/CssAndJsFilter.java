package cn.itcast.travel.web.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Si6x
 */
@WebFilter("/*")
public class CssAndJsFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        response.setCharacterEncoding("utf-8");
        boolean filterCssOrJs = uri.contains(".css") || uri.contains(".js") || uri.contains(".png");
        if(filterCssOrJs)
        {
            response.setContentType("text/css;charset=utf-8");
        }
        else
        {
            //处理响应乱码
            response.setContentType("text/html;charset=utf-8");
        }

        chain.doFilter(request,response);
    }
}
