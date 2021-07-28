package com.hs.dianping.dal;

import com.hs.dianping.model.ShopModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface ShopModelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Sun Jul 11 09:39:28 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Sun Jul 11 09:39:28 CST 2021
     */
    int insert(ShopModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Sun Jul 11 09:39:28 CST 2021
     */
    int insertSelective(ShopModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Sun Jul 11 09:39:28 CST 2021
     */
    ShopModel selectByPrimaryKey(Integer id);

    List<ShopModel> selectAll();

    Integer countAllShop();

    List<ShopModel> recommend(@Param("longitude") BigDecimal longitude, @Param("latitude") BigDecimal latitude);

    List<ShopModel> search(@Param("longitude") BigDecimal longitude,
                           @Param("latitude") BigDecimal latitude,
                           @Param("keyword")String keyword,
                           @Param("orderby")Integer orderBy,
                           @Param("categoryId")Integer categoryId,
                           @Param("tags")String tags);
    List<Map<String ,Object>> searchGroupByTags(@Param("keyword")String keyword,
                                                @Param("categoryId")Integer categoryId,
                                                @Param("tags")String tags);

    List<Map<String,Object>> buildESQuery(@Param("sellerId")Integer sellerId,@Param("categoryId")Integer categoryId,
                                          @Param("shopId")Integer shopId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Sun Jul 11 09:39:28 CST 2021
     */
    int updateByPrimaryKeySelective(ShopModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Sun Jul 11 09:39:28 CST 2021
     */
    int updateByPrimaryKey(ShopModel record);
}