package com.hs.dianping.controller.admin;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hs.dianping.common.*;
import com.hs.dianping.model.SellerModel;
import com.hs.dianping.request.PageQuery;
import com.hs.dianping.request.SellerCreateRequest;
import com.hs.dianping.service.Impl.SellerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller("/admin/seller")
@RequestMapping("/admin/seller")
public class SellerController {
    @Autowired
    private SellerServiceImpl sellerService;

//商户列表
    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery){
        //分页实现，直接在查询之前，调用PageHelper即可
        PageHelper.startPage(pageQuery.getPage(),pageQuery.getSize());

        List<SellerModel> sellerModelList=sellerService.selectAll();
        PageInfo<SellerModel> sellerModelPageInfo=new PageInfo<>(sellerModelList);

        ModelAndView modelAndView=new ModelAndView("/admin/seller/index.html");
        modelAndView.addObject("data",sellerModelList);
        modelAndView.addObject("CONTROLLER_NAME","admin");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }

    @RequestMapping("/createpage")
    @AdminPermission
    public ModelAndView createPage(){
        ModelAndView modelAndView=new ModelAndView("/admin/seller/create.html");
        modelAndView.addObject("CONTROLLER_NAME","seller");
        modelAndView.addObject("ACTION_NAME","create");
        return modelAndView;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid SellerCreateRequest sellerCreateRequest, BindingResult bindingResult) throws BusinessException {
        if(bindingResult.hasErrors()){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }
        SellerModel sellerModel=new SellerModel();
        sellerModel.setName(sellerCreateRequest.getName());
        sellerService.create(sellerModel);
        return "redirect:/admin/seller/index";
    }

    @RequestMapping(value = "/down",method = RequestMethod.POST)
    @AdminPermission
    @ResponseBody
    public CommonRes down(@RequestParam(value = "id")Integer id) throws BusinessException {
        SellerModel sellerModel=sellerService.changeStatus(id,1);
        return CommonRes.create(sellerModel);
    }

    @RequestMapping(value = "/up",method = RequestMethod.POST)
    @AdminPermission
    @ResponseBody
    public CommonRes up(@RequestParam(value = "id")Integer id) throws BusinessException {
        SellerModel sellerModel=sellerService.changeStatus(id,0);
        return CommonRes.create(sellerModel);
    }



}
