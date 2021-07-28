package com.hs.dianping.configure;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class ElasticSearchRestClient {
    //创建和ES服务端链接的客户端
    @Value("${elasticsearch.ip}")
    String ipAddress;

    @Bean(name = "highLevelClient")
    public RestHighLevelClient highLevelClient(){
        String[] address=ipAddress.split(":");
        String ip=address[0];
        Integer port=Integer.valueOf(address[1]);
        HttpHost httpHost=new HttpHost(ip,port,"http");
        return new RestHighLevelClient(RestClient.builder(new HttpHost[]{httpHost}));

    }
}
