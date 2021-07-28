package com.hs.dianping.common;

public class BusinessException extends Exception{
    private CommonError commonError;

    public CommonError getCommonError() {
        return commonError;
    }

    public void setCommonError(CommonError commonError) {
        this.commonError = commonError;
    }

    public BusinessException(EnumBusinessError enumBusinessError,String errMsg){
        super();
        this.commonError=new CommonError(enumBusinessError);
        this.commonError.setErrMsg(errMsg);
    }


    public BusinessException(EnumBusinessError enumBusinessError){
        super();
        this.commonError=new CommonError(enumBusinessError);
    }
}
