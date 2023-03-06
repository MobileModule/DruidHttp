package com.druid.http.rest;

import com.druid.http.DruidHttpLogger;
import com.druid.http.OkHttpCallBack;
import com.druid.http.RequestMethod;

import okhttp3.Response;

public abstract class DruidHttpRequest extends BaseHttpRequest {
    public DruidHttpRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);

    }

    public DruidHttpRequest(String url, RequestMethod requestMethod, String bodyContent) {
        super(url, requestMethod, bodyContent);
    }

    @Override
    public void execute(OkHttpCallBack callBack) {
        setRequestCall(AsyncRequestClient.INSTANCE.getClient().newCall(getRequestBuilder()));
        try {
            getRequestCall().enqueue(callBack);
        } catch (Exception ex) {
            DruidHttpLogger.e("http request error--->" + ex.getMessage());
        }
    }


    @Override
    public void cancel() {
        if (getRequestCall() != null) {
            cancelHand();
            getRequestCall().cancel();
        }
    }
}
