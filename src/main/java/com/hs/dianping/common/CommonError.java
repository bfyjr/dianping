package com.hs.dianping.common;

public class CommonError {
    private Integer errCode;
    private String errMsg;

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public CommonError(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
    public CommonError(EnumBusinessError enumBusinessError) {
        this.errCode = enumBusinessError.getErrCode();
        this.errMsg = enumBusinessError.getErrMsg();
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }


}
