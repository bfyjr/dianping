package com.hs.dianping.controller;

import com.hs.dianping.common.*;
import com.hs.dianping.model.UserModel;
import com.hs.dianping.request.LoginRequest;
import com.hs.dianping.request.RegisterRequest;
import com.hs.dianping.service.Impl.UserServiceImpl;
import com.hs.dianping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@Controller("/user")
@RequestMapping("/user")
public class UserController {
    public static final String CURRENT_USER_SESSION="currentUserSession";

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return "test";
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonRes getUser(@RequestParam(name="id")Integer id) throws BusinessException {
        UserModel user = userService.getUser(id);
        if(user==null){
            throw new BusinessException(EnumBusinessError.NO_OBJECTA_FOUND);
        }

        return CommonRes.create(user);
    }

    @RequestMapping("/index")
    public ModelAndView index(){
        String username="hs";
        ModelAndView modelAndView=new ModelAndView("/index2.html");
        modelAndView.addObject("name",username);
        return modelAndView;
    }

    @RequestMapping("/register")
    @ResponseBody
    public CommonRes register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) throws BusinessException, NoSuchAlgorithmException {
        if(bindingResult.hasErrors()){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }
        UserModel registerUserModel=new UserModel();
        registerUserModel.setTelphone(registerRequest.getTelphone());
        registerUserModel.setPassword(registerRequest.getPassword());
        registerUserModel.setNickName(registerRequest.getNickName());
        registerUserModel.setGender(registerRequest.getGender());

        userService.register(registerUserModel);
        return CommonRes.create(registerUserModel);
    }
    @RequestMapping("/login")
    @ResponseBody
    public CommonRes login(@RequestBody @Valid LoginRequest loginRequest,BindingResult bindingResult) throws BusinessException, NoSuchAlgorithmException {
        if(bindingResult.hasErrors()){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,
                    CommonUtil.processErrorString(bindingResult));
        }
        UserModel userModel=userService.login(loginRequest.getTelphone(),loginRequest.getPassword());
        //登陆成功之后，将该用户设置进session
        httpServletRequest.getSession().setAttribute(CURRENT_USER_SESSION,userModel);
        return CommonRes.create(userModel);
    }
    //登出
    @RequestMapping("/logout")
    @ResponseBody
    public CommonRes login() throws BusinessException, NoSuchAlgorithmException {
        //使session内所有的session无效
        httpServletRequest.getSession().invalidate();
        return CommonRes.create(null);
    }
    @RequestMapping("/getcurrentuser")
    @ResponseBody
    public CommonRes getCurrentUser(){
        UserModel userModel= (UserModel) httpServletRequest.getSession().getAttribute(CURRENT_USER_SESSION);
        return CommonRes.create(userModel);
    }
}
