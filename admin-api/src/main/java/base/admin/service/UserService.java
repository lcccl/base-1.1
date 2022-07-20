package base.admin.service;


import base.admin.model.po.User;

/**
 * Created by cl on 2017/11/17.
 * 用户服务
 */
public interface UserService {

    /**
     * 根据用户名查找用户
     */
    User findByUserName(String userName);

    /**
     * 保存用户
     */
    User saveUser(User user);

    /**
     * 更新用户
     */
    User updateUser(User user);

    /**
     * 更新用户密码
     */
    User updatePassword(String userName, String oldPassword, String password);

}
