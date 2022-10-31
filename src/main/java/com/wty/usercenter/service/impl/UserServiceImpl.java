package com.wty.usercenter.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wty.usercenter.commen.ErrorCode;
import com.wty.usercenter.exception.BusinessException;
import com.wty.usercenter.model.domain.User;
import com.wty.usercenter.model.request.UserRegisterRequest;
import com.wty.usercenter.service.UserService;
import com.wty.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wty.usercenter.constant.UserConstant.USER_LOGIN_STATE;


/**
 * @author wty
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2022-10-26 16:04:05
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private static final String SALT = "ABC";

    /**
     * 用户注册
     * @param userRegisterRequest User封装类
     * @return 用户ID
     */
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null){
            return -1;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        //1 校验不为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
        }
        // 账号不小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"账号过短");
        }
        // 密码不小于八位
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"密码过短");
        }
        //星球编号不能大于5位
        if (planetCode.length() > 5){
            throw new BusinessException(ErrorCode.PARAM_ERROR,"星球编号过长");
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"两次密码不一致");
        }
        // 账户不包含特殊字符
        String pattern = ".*[*?!&￥$%^#,./@\";:><\\]\\[}{\\-=+_\\\\|》《。，、？’‘“”~`）].*$";
        Matcher matcher = Pattern.compile(pattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"账号包含特殊字符");
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"账号已存在");
        }
        //星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"星球编号已存在");
        }
        //2 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3 插入数据
        User user = new User();
        user.setUsername("");
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setUserRole(0);
        user.setIsDelete(0);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"添加用户失败");
        }
        return user.getId();
    }

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request HttpServletRequest
     * @return User
     */
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1 校验账户账号、密码合法性
        //账号密码非空校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
           throw new BusinessException(ErrorCode.NULL_ERROR, "账号密码为空");
        }
        // 账号不小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号过短");
        }
        // 密码不小于八位
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"密码过短");
        }
        // 账户不包含特殊字符
        String pattern = ".*[*?!&￥$%^#,./@\";:><\\]\\[}{\\-=+_\\\\|》《。，、？’‘“”~`）].*$";
        Matcher matcher = Pattern.compile(pattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR,"账号包含特殊字符");
        }
        //2 校验密码是否输入正确，与数据库中密文对比
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = this.getOne(queryWrapper);
        if (user == null){
            log.info("userAccount cannot match");
            throw new BusinessException(ErrorCode.NULL_ERROR,"账号密码错误");
        }
        //3 脱敏
        User safetyUser = getSafetyUser(user);
        //4 记录用户的登录状态（session）
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        //5 返回用户信息（脱敏）
        return safetyUser;
    }

    /**
     * 脱敏
     * @param originUser User
     * @return safetyUser
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"参数为空");
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @Override
    public int logOut(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




