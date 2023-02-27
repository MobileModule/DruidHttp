package com.druid.http.rest;

import com.druid.http.DruidHttpHeaders;

import java.io.Serializable;

public class DruidHttpResponse implements Serializable {
    private int code_ = 0;

    public int code() {
        return code_;
    }

    public void setCode(int code) {
        this.code_ = code;
    }

    private String content_ = "";

    public String content() {
        return content_;
    }

    public void setContent(String content) {
        this.content_ = content;
    }

    private DruidHttpHeaders headers_ = null;

    public DruidHttpHeaders getHeaders() {
        return headers_;
    }

    public void setHeaders(DruidHttpHeaders headers) {
        this.headers_ = headers;
    }

    private boolean succeedStatus = false;

    public boolean isSucceed() {
        return succeedStatus;
    }

    public void setSucceed(boolean status) {
        this.succeedStatus = status;
    }

    private DruidHttpRequest request = null;

    public void setRequest(DruidHttpRequest request) {
        this.request = request;
    }

    public DruidHttpRequest getRequest() {
        return request;
    }
}
