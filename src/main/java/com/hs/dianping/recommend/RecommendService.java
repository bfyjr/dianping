package com.hs.dianping.recommend;

import com.hs.dianping.dal.RecommendDoMapper;
import com.hs.dianping.model.RecommendDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendService {
    @Autowired
    RecommendDoMapper recommendDoMapper;
    //召回数据
    public List<Integer> recall(int userId){
        RecommendDo recommendDo = recommendDoMapper.selectByPrimaryKey(userId);
        if(recommendDo==null){
            //没有推荐信息就使用存储的默认推荐
            recommendDo=recommendDoMapper.selectByPrimaryKey(99999);
        }
        //获取推荐的门店
        String[] shopIdStr=recommendDo.getRecommend().split(",");
        List<Integer> shopIdList=new ArrayList<>();
        for (int i = 0; i < shopIdStr.length; i++) {
            shopIdList.add(Integer.parseInt(shopIdStr[i]));
        }
        return shopIdList;
    }

}
