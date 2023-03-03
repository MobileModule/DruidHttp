package com.druid.http.rest;

import com.druid.http.DruidHttp;
import com.druid.http.DruidHttpConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public enum AsyncRequestClient {
    INSTANCE;

    OkHttpClient okHttpClient;
    AsyncRequestClient() {
        DruidHttpConfig config = DruidHttp.getInitializeConfig();
        okHttpClient=newHttpClient(config);
    }

    private OkHttpClient newHttpClient(DruidHttpConfig config) {
        return new OkHttpClient.Builder()
                .sslSocketFactory(config.getSSLSocketFactory())
                .followRedirects(config.getFollowRedirects())
                .hostnameVerifier(config.getHostnameVerifier())
                .connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }


    public OkHttpClient getClient(){
        return okHttpClient;
    }

    public OkHttpClient setConnectTimeout(long timeout){
        return okHttpClient;
    }
}
