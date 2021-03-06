package com.hs.dianping.dal;

import com.hs.dianping.model.CategoryModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryModelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category
     *
     * @mbg.generated Sat Jul 10 20:39:11 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category
     *
     * @mbg.generated Sat Jul 10 20:39:11 CST 2021
     */
    int insert(CategoryModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category
     *
     * @mbg.generated Sat Jul 10 20:39:11 CST 2021
     */
    int insertSelective(CategoryModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category
     *
     * @mbg.generated Sat Jul 10 20:39:11 CST 2021
     */
    CategoryModel selectByPrimaryKey(Integer id);

    List<CategoryModel> selectAll();

    Integer countAllCategory();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category
     *
     * @mbg.generated Sat Jul 10 20:39:11 CST 2021
     */
    int updateByPrimaryKeySelective(CategoryModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table category
     *
     * @mbg.generated Sat Jul 10 20:39:11 CST 2021
     */
    int updateByPrimaryKey(CategoryModel record);
}