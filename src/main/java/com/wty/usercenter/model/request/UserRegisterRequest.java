package com.wty.usercenter.model.request;


import lombok.Data;

import java.io.Serializable;

/**
 * 请求参数封装类
 * @author wty
 */
@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = 6149975081483486918L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
