package com.hs.dianping.controller;

import com.hs.dianping.common.CommonRes;
import com.hs.dianping.model.CategoryModel;
import com.hs.dianping.service.Impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//用于用户端接入
@Controller("/category")
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryServiceImpl categoryService;

    @ResponseBody
    @RequestMapping("/list")
    public CommonRes list(){
        List<CategoryModel> categoryModelList=categoryService.selectAll();
        return CommonRes.create(categoryModelList);
    }
}
