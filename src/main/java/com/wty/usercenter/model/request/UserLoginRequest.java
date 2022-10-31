package com.wty.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求参数封装类
 * @author wty
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 7161081939513701025L;

    private String userAccount;
    private String userPassword;
}
