package com.wty.usercenter.commen;

public class ResultUtils<T> {
    public static <T> BaseResponse<T> success(T result){
        return new BaseResponse(0000,"成功",result);
    }
    public static  BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }
    public static  BaseResponse error(ErrorCode errorCode,String message,String desc){
        return new BaseResponse<>(errorCode,message,desc);
    }
    public static  BaseResponse error(int errorCode,String message,String desc){
        return new BaseResponse<>(errorCode,message,null,desc);
    }
}
