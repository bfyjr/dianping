package com.hs.dianping.request;

import javax.validation.constraints.NotBlank;

public class SellerCreateRequest {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank(message = "商户名不能为空")
    private String name;
}
