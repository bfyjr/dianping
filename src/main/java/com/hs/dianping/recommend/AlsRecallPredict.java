package com.hs.dianping.recommend;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

//这也是个离线任务
public class AlsRecallPredict {

    public static void main(String[] args) {
        //初始化spark环境
        SparkSession spark=
                SparkSession.builder().master("local").appName("DianpingApp").getOrCreate();
        JavaRDD<String> csvFile=spark.read().textFile("D:///als/behavior.csv").toJavaRDD();

        JavaRDD<ALSRecallTrain.Rating> ratingJavaRDD=csvFile.map(new Function<String, ALSRecallTrain.Rating>() {
            @Override
            public ALSRecallTrain.Rating call(String s) throws Exception {
                return ALSRecallTrain.Rating.parseRating(s);
            }
        });
        Dataset<Row> rating = spark.createDataFrame(ratingJavaRDD, ALSRecallTrain.Rating.class);
        //训练测试数据82分
        Dataset<Row>[] splits = rating.randomSplit(new double[]{0.8, 0.2});
        Dataset<Row> trainingData = splits[0];
        Dataset<Row> testingData = splits[1];

        //过拟合：增大数据规模，减少rank规模，增大正则化系数
        ALS als=new ALS().setMaxIter(10).setRank(5).setRegParam(0.01)
                .setUserCol("userId").setItemCol("shopId").setRatingCol("rating");
        //得到预测模型
        ALSModel alsModel = als.fit(trainingData);


        ////选取用户离线预测召回结果
        Dataset<Row> users = rating.select(alsModel.getUserCol()).distinct().limit(5);
        //使用训练好的模型，获取每个用户的推荐结果
        Dataset<Row> userRecommends = alsModel.recommendForUserSubset(users, 20);
        //不同的部分可能在不同的机器上执行
        userRecommends.foreachPartition(new ForeachPartitionFunction<Row>() {
            @Override
            public void call(Iterator<Row> iterator) throws Exception {
                Connection connection= DriverManager
                        .getConnection("jdbc:mysql://localhost:3306/dianping?user=root&password=root&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
                PreparedStatement preparedStatement = connection.prepareStatement("insert into recommend(id,recommend) values (?,?) ");
                List<Map<String,Object>> data=new ArrayList<>();

                iterator.forEachRemaining(action->{
                    int userId=action.getInt(0);
                    List<GenericRowWithSchema> recommendList=action.getList(1);
                    List<Integer> shopIdList=new ArrayList<>();
                    recommendList.forEach(row->{
                        Integer shopId=row.getInt(0);
                        shopIdList.add(shopId);
                    });
                    String recommendData= StringUtils.join(shopIdList,",");
                    Map<String ,Object> map=new HashMap<>();
                    map.put("userId",userId);
                    map.put("recommend",recommendData);
                    data.add(map);
                });
            data.forEach(stringObjectMap -> {
                try {
                    preparedStatement.setInt(1,(Integer) stringObjectMap.get("userId"));
                    preparedStatement.setString(2,(String)stringObjectMap.get("recommend"));
                    preparedStatement.addBatch();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            preparedStatement.executeBatch();
            connection.close();
            }
        });
    }
}
