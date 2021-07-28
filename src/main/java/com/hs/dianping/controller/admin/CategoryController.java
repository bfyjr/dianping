package com.hs.dianping.controller.admin;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hs.dianping.common.AdminPermission;
import com.hs.dianping.common.BusinessException;
import com.hs.dianping.common.CommonUtil;
import com.hs.dianping.common.EnumBusinessError;
import com.hs.dianping.model.CategoryModel;
import com.hs.dianping.model.SellerModel;
import com.hs.dianping.request.CategoryCreateRequest;
import com.hs.dianping.request.PageQuery;
import com.hs.dianping.request.SellerCreateRequest;
import com.hs.dianping.service.Impl.CategoryServiceImpl;
import com.hs.dianping.service.Impl.SellerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
//用于服务端管理
@Controller("/admin/category")
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    //商户列表
    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery){
        //分页实现，直接在查询之前，调用PageHelper即可
        PageHelper.startPage(pageQuery.getPage(),pageQuery.getSize());

        List<CategoryModel> categoryModelList=categoryService.selectAll();
        PageInfo<CategoryModel> categoryModelPageInfo=new PageInfo<>(categoryModelList);

        ModelAndView modelAndView=new ModelAndView("/admin/category/index.html");
        modelAndView.addObject("data",categoryModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME","category");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }

    @RequestMapping("/createpage")
    @AdminPermission
    public ModelAndView createPage(){
        ModelAndView modelAndView=new ModelAndView("/admin/category/create.html");
        modelAndView.addObject("CONTROLLER_NAME","category");
        modelAndView.addObject("ACTION_NAME","create");
        return modelAndView;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid CategoryCreateRequest categoryCreateRequest, BindingResult bindingResult) throws BusinessException {
        if(bindingResult.hasErrors()){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }
        CategoryModel categoryModel=new CategoryModel();
        categoryModel.setName(categoryCreateRequest.getName());
        categoryModel.setIconUrl(categoryCreateRequest.getIconUrl());
        categoryModel.setSort(categoryCreateRequest.getSort());
        categoryService.create(categoryModel);
        return "redirect:/admin/category/index";
    }
}
