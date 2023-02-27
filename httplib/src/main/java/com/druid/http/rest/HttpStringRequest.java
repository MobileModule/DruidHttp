package com.druid.http.rest;

import com.druid.http.RequestMethod;


public class HttpStringRequest extends DruidHttpRequest{
    public HttpStringRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    public HttpStringRequest(String url, RequestMethod requestMethod, String bodyContent) {
        super(url, requestMethod, bodyContent);
    }

}
