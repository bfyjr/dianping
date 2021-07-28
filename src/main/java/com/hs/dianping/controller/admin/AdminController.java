package com.hs.dianping.controller.admin;


import com.alibaba.druid.util.StringUtils;
import com.hs.dianping.common.AdminPermission;
import com.hs.dianping.common.BusinessException;
import com.hs.dianping.common.EnumBusinessError;
import com.hs.dianping.service.CategoryService;
import com.hs.dianping.service.Impl.SellerServiceImpl;
import com.hs.dianping.service.ShopService;
import com.hs.dianping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Controller("/admin/admin")
@RequestMapping("/admin/admin")
public class AdminController {

    @Value("${admin.email}")
    private String email;
    @Value("${admin.encryptPassword}")
    private String encryptPassword;

    public static final String CURRENT_ADMIN_SESSION="currentAdminSession";

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ShopService shopService;

    @Autowired
    SellerServiceImpl sellerService;
    //首页必须登录才能放行，因此加上@AdminPermission
    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(){
        ModelAndView modelAndView=new ModelAndView("/admin/admin/index");
        modelAndView.addObject("CONTROLLER_NAME","admin");
        modelAndView.addObject("ACTION_NAME","index");
        modelAndView.addObject("userCount",userService.countAllUSer());
        modelAndView.addObject("shopCount",shopService.countAllShop());
        modelAndView.addObject("sellerCount",sellerService.countAllSeller());
        modelAndView.addObject("categoryCount",categoryService.countAllCategory());
        return modelAndView;
    }
    @RequestMapping("/loginpage")
    public ModelAndView loginPage(){
        ModelAndView modelAndView=new ModelAndView("/admin/admin/login");
        return modelAndView;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam(name = "email")String email,@RequestParam(name = "password")String password) throws BusinessException, NoSuchAlgorithmException {
        if(StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,"邮箱或者密码不能为空");
        }
        if(email.equals(this.email)&& encodeByMD5(password).equals(this.encryptPassword)){
            //登陆成功,将已经登陆的放入session
            httpServletRequest.getSession().setAttribute(CURRENT_ADMIN_SESSION,email);
            //重定向到首页
            return "redirect:/admin/admin/index";
        }else {
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,"登陆失败，用户名或密码错误");
        }
    }
    private String encodeByMD5(String pass) throws NoSuchAlgorithmException {
        MessageDigest messageDigest=MessageDigest.getInstance("MD5");
        return Arrays.toString(Base64Coder.encode(messageDigest.digest(pass.getBytes(StandardCharsets.UTF_8))));
    }


}
