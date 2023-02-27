package com.druid.http;

import com.druid.http.rest.DruidHttpResponse;

public interface HttpListener {
    void onSucceed(int what, DruidHttpResponse response);

    void onFailed(int what, DruidHttpResponse response);
}
