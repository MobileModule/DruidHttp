package com.druid.http;

import android.content.Context;

import com.druid.http.rest.DruidHttpRequest;
import com.druid.http.rest.HttpStringRequest;
import com.druid.http.rest.RequestQueue;

public class DruidHttp {
    private static DruidHttpConfig sInitializeConfig;

    private DruidHttp() {
    }

    public static void initialize(Context context) {
        initialize(DruidHttpConfig.newBuilder(context).build());
    }

    public static void initialize(DruidHttpConfig initializeConfig) {
        sInitializeConfig = initializeConfig;
    }

    private static void testInitialize() {
        if (sInitializeConfig == null)
            throw new ExceptionInInitializerError("Please invoke DruidHttp.initialize(Application) on Application#onCreate()");
    }

    public static Context getContext() {
        testInitialize();
        return sInitializeConfig.getContext();
    }

    public static DruidHttpConfig getInitializeConfig() {
        testInitialize();
        return sInitializeConfig;
    }

    public static RequestQueue newRequestQueue() {
        return newRequestQueue(5);
    }

    public static RequestQueue newRequestQueue(int threadPoolSize) {
        RequestQueue requestQueue = new RequestQueue(threadPoolSize);
        requestQueue.start();
        return requestQueue;
    }

    public static DruidHttpRequest createRequest(String url, RequestMethod requestMethod) {
        return new HttpStringRequest(url, requestMethod);
    }

    public static DruidHttpRequest createRequest(String url, RequestMethod requestMethod, String bodyContent) {
        return new HttpStringRequest(url, requestMethod, bodyContent);
    }

}
