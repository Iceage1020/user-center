package com.wty.usercenter.commen;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @author wty
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 6364302862996754742L;

    private int code;
    private String message;
    private T data;

    private String desc;

    public BaseResponse(int code, String message, T data,String desc) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.desc = desc;
    }
    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public BaseResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }
    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),errorCode.getMessage(),null,errorCode.getDesc());
    }
    public BaseResponse(ErrorCode errorCode,String message,String desc){
        this(errorCode.getCode(),message,null,desc);
    }
}
