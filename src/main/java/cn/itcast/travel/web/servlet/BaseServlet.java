package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Si6x
 */
public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("baseServlet的service方法被执行了...");

        //完成方法分发
        //1.获取请求路径
        // /travel/user/add
        String uri = request.getRequestURI();
//        System.out.println("请求uri:" + uri);  // /travel/user/add
        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
//        System.out.println("方法名称:" + methodName);
        //3.获取方法对象method
        //谁调用我？我代表谁
//        System.out.println(this);  // UserServlet的对象cn.itcast.travel.web.servlet.UserServlet@14a57cdf
        try {
            //获取方法
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //4.执行方法
            //暴力反射
            //method.setAccessible(true);
            method.invoke(this,request,response);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接将传入的对象序列化为json，并且写回客户端
     * @param obj
     */
    public void writeValue(Object obj, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),obj);
    }

    /**
     * 将传入的对象序列化为json，并返回给调用者
     * @param obj
     * @return
     */
    public String writeValueAsString(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    /**
     * 重载方法，向客户端浏览器回传cookie
     * @param cookie cookie对象
     * @param maxAge cookie生命周期
     * @param path cookie作用域
     * @param response http响应对象
     */
    public void writeCookie(Cookie cookie, int maxAge, String path, HttpServletResponse response){
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    /**
     * 将获取到的数据变成Int类型,如果没有数据设置默认值
     * @param parameterNameStr 获取浏览器的属性数据名称
     * @param num 设定默认值
     * @return 转为Int类型
     */
    public int parseInt(String parameterNameStr, int num){
        int parameterNameInt = 0;
        if (parameterNameStr != null && parameterNameStr.length() > 0){
            parameterNameInt = Integer.parseInt(parameterNameStr);
        }else {
            parameterNameInt = num;
        }
        return parameterNameInt;
    }
}
