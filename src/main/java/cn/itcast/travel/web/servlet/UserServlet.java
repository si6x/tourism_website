package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")  //user/add  user/find
public class UserServlet extends BaseServlet{
    //声明UserService业务对象
    private final UserService userService = new UserServiceImpl();
    private final FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证校验
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session = request.getSession();
        String checkcodeServer = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次
        //比较
        if (checkcodeServer == null || !checkcodeServer.equalsIgnoreCase(check)){
            //验证码错误
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            /*ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);*/
            writeValue(info,response);
            return;
        }


        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用service完成注册
//        UserService service = new UserServiceImpl();
        boolean flag = userService.regist(user);
        ResultInfo info = new ResultInfo();
        //4.响应结果
        if (flag){
            //注册成功
            info.setFlag(true);
        }else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败!");
        }

        //将info对象序列化为json
        /*ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        //将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);*/
        writeValue(info,response);
    }

    /**
     * 激活用户功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码
        String code = request.getParameter("code");
        if (code != null){
            //2.调用service完成激活
//            UserService service = new UserServiceImpl();
            boolean flag = userService.active(code);
            //3.判断标记
            String msg = null;
            if (flag){
                //激活成功
                msg = "激活成功，请<a href = '../login.html'>登录</a>";
            }else {
                //激活失败
                msg = "激活失败，请联系管理员！";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

    /**
     * 登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户输入的验证码
        String check = request.getParameter("check");
        //获取服务器生成的验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER"); //删除session中存储的验证码防止重复使用
        //判断验证码是否正确
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
            //验证码错误
            ResultInfo info = new ResultInfo();
            //登录失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            /*ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);*/
            writeValue(info,response);
            return;
        }

        //获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();
        //封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //调用service查询
//        UserService service = new UserServiceImpl();
        User loginuser = userService.login(user);
        ResultInfo info = new ResultInfo();
        //判断用户对象是否为null
        if (loginuser == null){
            //登录失败
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        //判断用户是否激活
        if (loginuser != null && !"Y".equals(loginuser.getStatus())){
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活用户,请激活");
        }
        //判断登录成功
        if (loginuser != null && "Y".equals(loginuser.getStatus())){
            //登录成功
            //将用户存入session域中,让首页获取用户名
            request.getSession().setAttribute("loginuser",loginuser);
            info.setFlag(true);
        }
        //判断用户是否勾选'自动登录'
        if (map.containsKey("auto_login")){
            //用户勾选了自动登录
            //创建一个Cookie对象，设置JSESSIONID属性值为当前session对象的id值
            Cookie jSessionIdCookie = new Cookie("JSESSIONID",session.getId());
            //将该Cookie对象回写给浏览器，并设置maxAge令浏览器持久化保存用户登录的状态，设置作用域为"/",即全域生效
            this.writeCookie(jSessionIdCookie,60 * 60 * 24 * 15 ,"/",response);

        }
        //响应数据
        /*ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getWriter(),info);*/
        writeValue(info,response);
    }

    /**
     * 获取登录用户功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getLoginuser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中获取登录用户
        Object loginuser = request.getSession().getAttribute("loginuser");
        if (loginuser != null){
            //将user写回客户端
            /*ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json;charset=utf-8");
            mapper.writeValue(response.getWriter(),loginuser);*/
            writeValue(loginuser,response);
        }

    }

    /**
     * 退出登录 功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //销毁session
        request.getSession().invalidate();
        //跳转到首页,重定向
        response.sendRedirect(request.getContextPath() + "/index.html");
    }

    /**
     * 我的收藏 功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findMyFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取session,取得用户对象
        HttpSession session = request.getSession();
        User loginuser = (User) session.getAttribute("loginuser");
        if (loginuser == null){
            //用户未登录
            writeValue(null,response);
            return;
        }
        //获取并处理浏览器请求属性
        String currentPageStr = request.getParameter("currentPage"); //当前页码
        String pageSizeStr = request.getParameter("pageSize"); //每页显示条数
        int currentPage = this.parseInt(currentPageStr,1);
        int pageSize = this.parseInt(pageSizeStr,16);
        //获取service,根据uid查rid再根据rid查详细数据
        PageBean<Route> routePageBean = favoriteService.favoritePageQuery(loginuser.getUid(),currentPage,pageSize);
        //将数据写回至浏览器
        writeValue(routePageBean,response);


    }

}
