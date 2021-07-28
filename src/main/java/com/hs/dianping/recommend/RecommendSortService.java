package com.hs.dianping.recommend;

import org.apache.spark.ml.classification.GBTClassificationModel;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendSortService {
    private SparkSession sparkSession;
    private LogisticRegressionModel logisticRegressionModel;
    private GBTClassificationModel gbtClassificationModel;

    @PostConstruct
    public void init() throws IOException {
        //初始化spark环境
        SparkSession spark=
                SparkSession.builder().master("local").appName("DianpingApp").getOrCreate();
        //加载排序模型,简便起见直接运行时当场训练模型
        logisticRegressionModel=LRTrain.getLRModel();
        gbtClassificationModel=GBDTTrain.getGBDTModel();
    }

    public List<Integer> sort(List<Integer> shopIdList,Integer userId){
        List<ShopSortModel> shopSortModelList=new ArrayList<>();
        //根据LRmodel需要的11维的x生成特征，调用预测方法
        for(Integer shopId:shopIdList){
            //实际生产环境中，需要根据登录的用户的信息来构造x特征，这里使用模拟的
            Vector v=Vectors.dense(1,0,0,0,0,1,0.6,0,0,1,0);
            //预测正负结果的概率
//            Vector result = logisticRegressionModel.predictProbability(v);
            Vector result = gbtClassificationModel.predictProbability(v);
            double[] doubles = result.toArray();//正负
            double score=doubles[1];

            ShopSortModel shopSortModel=new ShopSortModel();
            shopSortModel.setShopId(shopId);
            shopSortModel.setScore(score);
            shopSortModelList.add(shopSortModel);
        }
        shopSortModelList.sort(new Comparator<ShopSortModel>() {
            @Override
            public int compare(ShopSortModel o1, ShopSortModel o2) {
                if (o1.getScore()<o2.getScore()){
                    return -1;
                }else if(o1.getScore()>o2.getScore()){
                    return 1;
                }else {
                    return 0;
                }
            }
        });
        return shopSortModelList.stream().map(shopSortModel -> shopSortModel.getShopId()).collect(Collectors.toList());
    }

}
