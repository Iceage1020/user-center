package com.wty.usercenter.exception;

import com.wty.usercenter.commen.ErrorCode;

public class BusinessException extends RuntimeException{

    private int code;
    private String desc;

    public BusinessException(String message, int code, String desc) {
        super(message);
        this.code = code;
        this.desc = desc;
    }
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.desc = errorCode.getDesc();
    }
    public BusinessException(ErrorCode errorCode,String desc) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.desc = desc;
    }
    public BusinessException(ErrorCode errorCode, String message,String desc) {
        super(message);
        this.code = errorCode.getCode();
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }
}
