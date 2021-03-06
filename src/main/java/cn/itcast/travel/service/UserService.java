package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * 注册用户,返回是否注册成功
     * @param user
     * @return
     */
    boolean regist(User user);


    /**
     * 激活用户
     * @param code
     * @return
     */
    boolean active(String code);


    /**
     * 登录方法
     * @param user
     * @return
     */
    User login(User user);

}
