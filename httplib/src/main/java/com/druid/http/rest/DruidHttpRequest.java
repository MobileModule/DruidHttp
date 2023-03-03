package com.druid.http.rest;

import com.druid.http.DruidHttpLogger;
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
    public Response execute() {
        setRequestCall(AsyncRequestClient.INSTANCE.getClient().newCall(getRequestBuilder()));
        Response response = null;
        try {
            response = getRequestCall().execute();
        } catch (Exception ex) {
            DruidHttpLogger.e("http request error--->" + ex.getMessage());
        }
        return response;
    }


    @Override
    public void cancel() {
        if (getRequestCall() != null)
            getRequestCall().cancel();
    }
}
