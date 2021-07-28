package com.hs.dianping.service;

import com.hs.dianping.common.BusinessException;
import com.hs.dianping.model.CategoryModel;

import java.util.List;

public interface CategoryService {
    CategoryModel create(CategoryModel categoryModel) throws BusinessException;
    CategoryModel get(Integer id);
    List<CategoryModel> selectAll();
    Integer countAllCategory();

}
