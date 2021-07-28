package com.hs.dianping.common;

import com.hs.dianping.controller.admin.AdminController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Aspect
@Configuration
public class ControllerAspect {
    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    HttpServletResponse httpServletResponse;

    //指定包下文件的所有带了RequestMapping注解的方法使用此切面
    @Around("execution(* com.hs.dianping.controller.admin.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object adminContorllerBeforeValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method=((MethodSignature)joinPoint.getSignature()).getMethod();
        AdminPermission annotation = method.getAnnotation(AdminPermission.class);
        if(annotation==null){
            //代表这个没有加AdminPermission注解，是公共方法，直接放行
            Object o=joinPoint.proceed();
            return o;
        }
        //判断当前管理员是否登录
        String email= (String) httpServletRequest.getSession().getAttribute(AdminController.CURRENT_ADMIN_SESSION);
        if(email==null){
            if(annotation.produceType().equals("text/html"))
            {
                //否则重定向回去登录
                httpServletResponse.sendRedirect("/admin/admin/loginpage");
                return null;
            }else{
                //"application/json格式的返回值
                CommonError commonError=new CommonError(EnumBusinessError.ADMIN_SHOULD_LOGIN);
                return CommonRes.create(commonError,"fail");
            }
        }else{
            //若登录就放行
            Object o=joinPoint.proceed();
            return o;
        }


    }
}
