package com.druid.http.rest;

import android.text.TextUtils;

import com.druid.http.DruidHttpHeaders;
import com.druid.http.DruidHttpLogger;

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


    @Override
    public String toString() {
        return "tag:" + request.getCancelSign() + "," +
                "url:" + request.getUrl() + "," +
                "respondCode:" + code_ + "," +
                "content:" + content_+","+
                "requestBody:" + request.getJsonBodyContent();
    }

    public String getCancelTag(){
        String tag= DruidHttpLogger.class.getName();
        try {
            Object cancelTag = request.getCancelSign();
            if (cancelTag != null) {
                if (cancelTag instanceof String) {
                    String tagStr = (String) cancelTag;
                    if (!TextUtils.isEmpty(tagStr)) {
                        tag = tagStr;
                    }
                }
            }
        }catch (Exception ex){

        }
        return tag;
    }

}
