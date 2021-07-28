package com.hs.dianping.common;

public class CommonRes {
    private String status;
    //status:success  data:返回的json
    //status:fail     data:错误码
    private Object data;

    //声明定义通用的创建返回对象的方法

    public static CommonRes create(Object result){
        return CommonRes.create(result,"success");
    }



    public static CommonRes create(Object result, String status){
        CommonRes commonRes=new CommonRes();
        commonRes.setStatus(status);
        commonRes.setData(result);
        return commonRes;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
