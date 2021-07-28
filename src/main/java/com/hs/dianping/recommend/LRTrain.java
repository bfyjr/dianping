package com.hs.dianping.recommend;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;

public class LRTrain {
    public static LogisticRegressionModel getLRModel() throws IOException {
        //初始化spark环境
        SparkSession spark =
                SparkSession.builder().master("local").appName("DianpingApp").getOrCreate();
        JavaRDD<String> csvFile = spark.read().textFile("D:///als/feature.csv").toJavaRDD();
        //转化成Row的DataSet
        JavaRDD<Row> rowJavaRDD = csvFile.map(new Function<String, Row>() {
            @Override
            public Row call(String s) throws Exception {
                s = s.replace("\"", "");
                String[] splits = s.split(",");

                return RowFactory.create(new Double(splits[11]), Vectors.dense(Double.valueOf(splits[0]), Double.valueOf(splits[1]),
                        Double.valueOf(splits[2]), Double.valueOf(splits[3]), Double.valueOf(splits[4]), Double.valueOf(splits[5]),
                        Double.valueOf(splits[6]), Double.valueOf(splits[7]), Double.valueOf(splits[8]), Double.valueOf(splits[9]),
                        Double.valueOf(splits[10])));
            }
        });
        //ALS算法使用自定义的，Rating，逻辑回归使用多维的x预测一个label
        StructType schema = new StructType(
                new StructField[]{
                        new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
                        new StructField("features", new VectorUDT(), false, Metadata.empty())
                }
        );

        Dataset<Row> data = spark.createDataFrame(rowJavaRDD, schema);
        //分开测试集和训练集
        Dataset<Row>[] dataArr = data.randomSplit(new double[]{0.8, 0.2});
        Dataset<Row> trainData = dataArr[0];
        Dataset<Row> testData = dataArr[1];

        LogisticRegression lr = new LogisticRegression().setMaxIter(10).setRegParam(0.3)
                .setElasticNetParam(0.8).setFamily("multinomial");
        LogisticRegressionModel lrModel = lr.fit(trainData);
//        lrModel.save("D:///als/lrModel");
        return lrModel;
    }




}
