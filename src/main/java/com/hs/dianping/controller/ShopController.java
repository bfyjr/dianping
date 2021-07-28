package com.hs.dianping.controller;

import com.alibaba.druid.util.StringUtils;
import com.hs.dianping.common.BusinessException;
import com.hs.dianping.common.CommonRes;
import com.hs.dianping.common.EnumBusinessError;
import com.hs.dianping.model.CategoryModel;
import com.hs.dianping.model.ShopModel;
import com.hs.dianping.service.Impl.CategoryServiceImpl;
import com.hs.dianping.service.Impl.ShopServiceImpl;
import com.hs.dianping.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/shop")
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    ShopService shopService;

    @Autowired
    CategoryServiceImpl categoryService;

    //推荐服务 v1.0
    @RequestMapping("/recommend")
    @ResponseBody
    public CommonRes recommend(@RequestParam(name = "longitude")BigDecimal longitude,
                               @RequestParam(name = "latitude")BigDecimal latitude) throws BusinessException {
        if(longitude==null || latitude==null){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,"经纬度不能为空");
        }
        List<ShopModel> shopModelList=shopService.recommend(longitude,latitude);
        return CommonRes.create(shopModelList);
    }

    //搜索服务 v1.0
    @RequestMapping("/search")
    @ResponseBody
    public CommonRes search(@RequestParam(name = "longitude")BigDecimal longitude,
                            @RequestParam(name = "latitude")BigDecimal latitude,
                            @RequestParam(name = "keyword")String keyword,
                            @RequestParam(name = "orderby",required = false)Integer orderBy,
                            @RequestParam(name = "categoryId",required = false)Integer categoryId,
                            @RequestParam(name = "tags",required = false)String tags) throws BusinessException, IOException {
        if(StringUtils.isEmpty(keyword) || longitude==null||latitude==null){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,"没有经纬度或者搜索信息");
        }
        Map<String,Object> result = shopService.searchES(longitude,latitude,keyword,orderBy,categoryId,tags);
        List<ShopModel> shopModelList = (List<ShopModel>) result.get("shop");
        List<CategoryModel> categoryModelList = categoryService.selectAll();
        //返回键值对类型的List用于展示在前端，若前端点击就会再次调用search方法并传进tag参数
        List<Map<String,Object>>  tagsAggregation = (List<Map<String, Object>>) result.get("tags");
        //需要扩展搜索条件，暂时放到map
        Map<String,Object> resMap=new HashMap<>();
        resMap.put("shop",shopModelList);
        resMap.put("category",categoryModelList);
        resMap.put("tags",tagsAggregation);
        return CommonRes.create(resMap);

    }



}
