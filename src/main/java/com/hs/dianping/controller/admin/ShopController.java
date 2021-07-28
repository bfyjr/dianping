package com.hs.dianping.controller.admin;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hs.dianping.common.AdminPermission;
import com.hs.dianping.common.BusinessException;
import com.hs.dianping.common.CommonUtil;
import com.hs.dianping.common.EnumBusinessError;
import com.hs.dianping.model.CategoryModel;
import com.hs.dianping.model.ShopModel;
import com.hs.dianping.request.CategoryCreateRequest;
import com.hs.dianping.request.PageQuery;
import com.hs.dianping.request.ShopCreateRequest;
import com.hs.dianping.service.Impl.CategoryServiceImpl;
import com.hs.dianping.service.Impl.ShopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller("/admin/shop")
@RequestMapping("/admin/shop")
public class ShopController {
    @Autowired
    private ShopServiceImpl  shopService;

    //门店列表
    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery){
        //分页实现，直接在查询之前，调用PageHelper即可
        PageHelper.startPage(pageQuery.getPage(),pageQuery.getSize());

        List<ShopModel> shopModelList=shopService.selectAll();
        PageInfo<ShopModel> shopModelPageInfo=new PageInfo<>(shopModelList);

        ModelAndView modelAndView=new ModelAndView("/admin/shop/index.html");
        modelAndView.addObject("data",shopModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME","shop");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }

    @RequestMapping("/createpage")
    @AdminPermission
    public ModelAndView createPage(){
        ModelAndView modelAndView=new ModelAndView("/admin/shop/create.html");
        modelAndView.addObject("CONTROLLER_NAME","shop");
        modelAndView.addObject("ACTION_NAME","create");
        return modelAndView;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid ShopCreateRequest shopCreateRequest, BindingResult bindingResult) throws BusinessException {
        if(bindingResult.hasErrors()){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }
        ShopModel shopModel=new ShopModel();
        shopModel.setIconUrl(shopCreateRequest.getIconUrl());
        shopModel.setAddress(shopCreateRequest.getAddress());
        shopModel.setCategoryId(shopCreateRequest.getCategoryId());
        shopModel.setEndTime(shopCreateRequest.getEndTime());
        shopModel.setStartTime(shopCreateRequest.getStartTime());
        shopModel.setLongitude(shopCreateRequest.getLongitude());
        shopModel.setLatitude(shopCreateRequest.getLatitude());
        shopModel.setName(shopCreateRequest.getName());
        shopModel.setPricePerMan(shopCreateRequest.getPricePerMan());
        shopModel.setSellerId(shopCreateRequest.getSellerId());

        shopService.create(shopModel);
        return "redirect:/admin/shop/index";
    }
}

