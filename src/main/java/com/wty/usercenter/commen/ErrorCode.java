package com.wty.usercenter.commen;

public enum ErrorCode {
    SUCCES(0000,"OK",""),
    PARAM_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求参数为空",""),
    NOT_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"无权限",""),
    NO_MATCH_RECORD(40102,"无符合条件记录",""),
    SYSTEM_ERRCODE(50000,"系统内部错误","");


    private final int code;
    private final String message;
    private final String desc;

    ErrorCode(int code, String message, String desc) {
        this.code = code;
        this.message = message;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDesc() {
        return desc;
    }
}
