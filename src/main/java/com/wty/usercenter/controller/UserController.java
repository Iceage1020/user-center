package com.wty.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wty.usercenter.commen.BaseResponse;
import com.wty.usercenter.commen.ErrorCode;
import com.wty.usercenter.commen.ResultUtils;
import com.wty.usercenter.model.domain.User;
import com.wty.usercenter.model.request.UserLoginRequest;
import com.wty.usercenter.model.request.UserRegisterRequest;
import com.wty.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wty.usercenter.constant.UserConstant.USER_LOGIN_STATE;
import static com.wty.usercenter.constant.UserConstant.USER_ROLE_ADMIN;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 请求参数封装类
     * @return 用户ID
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return ResultUtils.error(ErrorCode.PARAM_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        long userId = userService.userRegister(userRegisterRequest);
        return ResultUtils.success(userId);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest UserLoginRequest
     * @param request          HttpServletRequest
     * @return User
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        int result = userService.logOut(request);
        return ResultUtils.success(result);
    }


    /**
     * 管理员查询用户，用户名可以为空
     * @param username 用户名
     * @param request HttpServletRequest
     * @return List<User>
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> userSearch(String username, HttpServletRequest request) {
        boolean isAdmin = this.isAdmin(request);
        if (!isAdmin) {
            return ResultUtils.error(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username),"username", username);
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(userService::getSafetyUser).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 管理员根据用户ID删除用户
     * @param id 用户ID
     * @param request HttpServletRequest
     * @return boolean
     */
    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUser( long id, HttpServletRequest request) {
        boolean isAdmin = this.isAdmin(request);
        if (!isAdmin) {
            return ResultUtils.error(ErrorCode.NO_AUTH);
        }
        if (id > 0) {
            boolean b = userService.removeById(id);
            return ResultUtils.success(b);
        }
        return ResultUtils.error(ErrorCode.NO_MATCH_RECORD);
    }

    /**
     * 查询当前用户信息
     * @param request HttpServletRequest
     * @return User
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        User userCurrent = userService.getById(user.getId());
        User result = userService.getSafetyUser(userCurrent);
        return ResultUtils.success(result);
    }

    /**
     * 管理员判断
     * @param request HttpServletRequest
     * @return boolean
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user.getUserRole() != USER_ROLE_ADMIN) {
            return false;
        }
        return true;
    }


}
