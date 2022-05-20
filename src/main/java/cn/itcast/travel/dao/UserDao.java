package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

import java.util.List;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public User findByUsername(String username);

    /**
     * 保存用户信息
     * @param user
     */
    public void save(User user);

    /**
     * 查询激活码
     * @param code
     * @return
     */
    public User findByCode(String code);


    /**
     * 修改用户激活状态
     * @param user
     */
    public void updateStatus(User user);

    /**
     * 查询用户名和密码
     * @param username
     * @param password
     * @return
     */
    public User findByUsernameAndPassword(String username, String password);
}
