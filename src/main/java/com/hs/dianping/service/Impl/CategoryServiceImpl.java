package com.hs.dianping.service.Impl;

import com.hs.dianping.common.BusinessException;
import com.hs.dianping.common.EnumBusinessError;
import com.hs.dianping.dal.CategoryModelMapper;
import com.hs.dianping.model.CategoryModel;
import com.hs.dianping.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryModelMapper categoryModelMapper;

    @Override
    @Transactional
    public CategoryModel create(CategoryModel categoryModel) throws BusinessException {
        categoryModel.setCreatedAt(new Date());
        categoryModel.setUpdatedAt(new Date());
        try{
            categoryModelMapper.insertSelective(categoryModel);
        }catch (DuplicateKeyException e){
            throw new BusinessException(EnumBusinessError.CATEGORY_DUPLICATED_ERROR);
        }
        return get(categoryModel.getId());
    }

    @Override
    public CategoryModel get(Integer id) {
        return categoryModelMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CategoryModel> selectAll() {
        return categoryModelMapper.selectAll();
    }

    @Override
    public Integer countAllCategory() {
        return categoryModelMapper.countAllCategory();
    }
}
