package com.hs.dianping.dal;

import com.hs.dianping.model.RecommendDo;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table recommend
     *
     * @mbg.generated Thu Jul 22 21:49:23 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table recommend
     *
     * @mbg.generated Thu Jul 22 21:49:23 CST 2021
     */
    int insert(RecommendDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table recommend
     *
     * @mbg.generated Thu Jul 22 21:49:23 CST 2021
     */
    int insertSelective(RecommendDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table recommend
     *
     * @mbg.generated Thu Jul 22 21:49:23 CST 2021
     */
    RecommendDo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table recommend
     *
     * @mbg.generated Thu Jul 22 21:49:23 CST 2021
     */
    int updateByPrimaryKeySelective(RecommendDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table recommend
     *
     * @mbg.generated Thu Jul 22 21:49:23 CST 2021
     */
    int updateByPrimaryKey(RecommendDo record);
}