package com.hs.dianping.service;

import com.hs.dianping.common.BusinessException;
import com.hs.dianping.model.ShopModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ShopService {
    ShopModel create(ShopModel shopModel) throws BusinessException;

    ShopModel get(Integer id);

    List<ShopModel> selectAll();

    Integer countAllShop();

    List<ShopModel> recommend(BigDecimal longitude,BigDecimal latitude);

    List<ShopModel> search(BigDecimal longitude,BigDecimal latitude,String keyword,
                           Integer orderBy,Integer categoryId,String tags);

    List<Map<String,Object>> searchGroupByTags(String keyword,Integer categoryId,String tags);

    Map<String ,Object> searchES(BigDecimal longitude,BigDecimal latitude,String keyword,
                                 Integer orderBy,Integer categoryId,String tags) throws IOException;


}
