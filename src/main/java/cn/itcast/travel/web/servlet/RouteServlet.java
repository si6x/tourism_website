package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    //声明RouteService业务对象
    private final RouteService routeService = new RouteServiceImpl();
    private final FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        //接收rname参数 线路名称
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        //处理参数
        int currentPage = 0; //当前页码,如果不传递，默认为第一页
        if (currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }

        int pageSize = 0; //每页显示条数，如果不传递，默认每页显示10条记录
        if (pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            pageSize = 10;
        }

        int cid = 0; //类别id
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }



        //调用service查询PageBean对象
        PageBean<Route> routePageBean = routeService.pageQuery(currentPage, pageSize, cid, rname);

        //将PageBean对象序列化为json，返回
        writeValue(routePageBean,response);

    }

    /**
     * 获取rid并查询返回
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收rid
        String rid = request.getParameter("rid");
        //调用service查询
        Route route = routeService.findOne(rid);
        //转换json返回
        writeValue(route,response);
    }

    /**
     *判断当前登录用户是否收藏过该线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取线路id
        String rid = request.getParameter("rid");

        //2.获取当前登录的用户 user
        User loginuser = (User) request.getSession().getAttribute("loginuser");
        int uid; //用户id
        if (loginuser == null){
            //用户尚未登录
            return;
        }else {
            //用户已登录
            uid = loginuser.getUid();
        }

        //3.调用FavoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);

        //4.写回客户端
        writeValue(flag,response);


    }

    /**
     * 添加收藏,同时判断用户是否登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取线路rid
        String rid = request.getParameter("rid");
        //获取用户对象 uid
        User loginuser = (User) request.getSession().getAttribute("loginuser");
        //判断用户是否登录
        if (loginuser == null){
            return;
        }
        //用户已登录,调用service添加
        favoriteService.addFavorite(rid,loginuser.getUid());

    }

    /**
     * 取消收藏,同时判断用户是否登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void removeFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取路线rid
        String rid = request.getParameter("rid");
        //获取用户对象 uid
        User loginuser = (User) request.getSession().getAttribute("loginuser");
        //判断用户是否登录
        if (loginuser == null){
            return;
        }
        //用户已登录,调用service删除
        favoriteService.removeFavorite(rid,loginuser.getUid());
    }

}
