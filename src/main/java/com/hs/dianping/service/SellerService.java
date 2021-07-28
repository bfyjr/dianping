package com.hs.dianping.service;

import com.hs.dianping.common.BusinessException;
import com.hs.dianping.model.SellerModel;

import java.util.List;

public interface SellerService {
    SellerModel create(SellerModel sellerModel);
    SellerModel get(Integer id);
    List<SellerModel> selectAll();
    SellerModel changeStatus(Integer id,Integer disabledFlag) throws BusinessException;
    Integer countAllSeller();

}
