package com.wty.usercenter.service;

import com.wty.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wty.usercenter.model.request.UserRegisterRequest;

import javax.servlet.http.HttpServletRequest;

/**
* @author wty
* @description 针对表【user】的数据库操作Service
* @createDate 2022-10-26 16:04:05
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return 用户ID
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return User
     */
    User userLogin(String userAccount , String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser originUser
     * @return User
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int logOut(HttpServletRequest request);
}
