package com.hs.dianping.recommend;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.io.Serializable;

//召回算法的训练，csv文件是按照用户的评分已经收集好的
public class ALSRecallTrain implements Serializable {

    public static class Rating implements Serializable{
        private int userId;
        private int shopId;
        private int rating;

        public Rating(int userId, int shopId, int rating) {
            this.userId = userId;
            this.shopId = shopId;
            this.rating = rating;
        }

        public static void main(String[] args) throws IOException {
            //初始化spark环境
            SparkSession spark=
                    SparkSession.builder().master("local").appName("DianpingApp").getOrCreate();
            JavaRDD<String> csvFile=spark.read().textFile("D:///als/behavior.csv").toJavaRDD();

            JavaRDD<Rating> ratingJavaRDD=csvFile.map(new Function<String, Rating>() {
                @Override
                public Rating call(String s) throws Exception {
                    return Rating.parseRating(s);
                }
            });
            Dataset<Row> rating = spark.createDataFrame(ratingJavaRDD, Rating.class);
            //训练测试数据82分
            Dataset<Row>[] splits = rating.randomSplit(new double[]{0.8, 0.2});
            Dataset<Row> trainingData = splits[0];
            Dataset<Row> testingData = splits[1];

            //过拟合：增大数据规模，减少rank规模，增大正则化系数
            ALS als=new ALS().setMaxIter(10).setRank(5).setRegParam(0.01)
                    .setUserCol("userId").setItemCol("shopId").setRatingCol("rating");
            ALSModel alsModel = als.fit(trainingData);

            //模型评测,会得到一个预测列predictions
            Dataset<Row> predictions = alsModel.transform(testingData);
            //均方根误差

            RegressionEvaluator evaluator=new RegressionEvaluator().setMetricName("rmse")
                    .setLabelCol("rating").setPredictionCol("prediction");
            double rmse = evaluator.evaluate(predictions);



            alsModel.save("D:/als/alsmodel");


        }

        public static Rating parseRating(String str){
            str=str.replace("\"","");
            String[] strArr=str.split(",");
            int userId=Integer.parseInt(strArr[0]);
            int shopId=Integer.parseInt(strArr[1]);
            int rating=Integer.parseInt(strArr[2]);

            return new Rating(userId,shopId,rating);
        }

        public int getUserId() {
            return userId;
        }

        public int getShopId() {
            return shopId;
        }

        public int getRating() {
            return rating;
        }
    }
}

