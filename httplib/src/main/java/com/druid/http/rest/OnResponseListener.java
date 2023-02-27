package com.druid.http.rest;


public interface OnResponseListener {
    void onStart(int what);

    void onSucceed(int what, DruidHttpResponse response);

    void onFailed(int what, DruidHttpResponse response);

    void onFinish(int what);
}
