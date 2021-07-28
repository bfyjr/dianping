package com.hs.dianping.common;

public enum EnumBusinessError {
    //通用错误
    NO_OBJECTA_FOUND(10001,"请求对象不存在"),
    UNKNOWN_ERROR(10002,"未知错误"),
    NO_HANDLER_FOUND(10003,"找不到执行路径"),
    PARAM_ERROR(10004,"路径参数错误"),
    PARAMETER_VALIDATION_ERROR(10005,"请求参数校验失败"),
//用户相关错误
    REGISTER_DUP_FAIL(20001,"手机号重复注册"),
    LOGIN_FAIL(20002,"手机或密码错误"),
    //管理员错误
    ADMIN_SHOULD_LOGIN(30001,"管理员需要先登录"),
    CATEGORY_DUPLICATED_ERROR(30002,"品类名已存在");


    EnumBusinessError(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    private Integer errCode;
    private String errMsg;
}
