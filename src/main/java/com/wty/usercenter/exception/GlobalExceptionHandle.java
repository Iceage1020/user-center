package com.wty.usercenter.exception;

import com.wty.usercenter.commen.BaseResponse;
import com.wty.usercenter.commen.ErrorCode;
import com.wty.usercenter.commen.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.ws.Response;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandle(BusinessException e){
        log.error("BusinessException",e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDesc());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandle(RuntimeException e){
        log.error("runtimeExceptionHandle",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERRCODE,e.getMessage(),"");
    }
}
