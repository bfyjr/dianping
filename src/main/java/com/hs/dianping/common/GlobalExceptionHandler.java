package com.hs.dianping.common;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//加注解代表所有Controller都会被这个切面去切
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonRes doError(HttpServletRequest request, HttpServletResponse response,Exception ex){
        if(ex instanceof BusinessException){
            return CommonRes.create(((BusinessException) ex).getCommonError(),"fail");
        }else if(ex instanceof NoHandlerFoundException){
            return CommonRes.create(new CommonError(EnumBusinessError.NO_HANDLER_FOUND),"fail");
        }else if(ex instanceof ServletRequestBindingException){
            return CommonRes.create(new CommonError(EnumBusinessError.PARAM_ERROR),"fail");
        }
        else {
            return CommonRes.create(new CommonError(EnumBusinessError.UNKNOWN_ERROR),"fail");
        }

    }


}
