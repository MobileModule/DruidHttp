package com.druid.http.rest;

import com.druid.http.DruidHttp;
import com.druid.http.DruidHttpConfig;

public enum SyncRequestExecutor {
    INSTANCE;

    private RequestHandler mRequestHandler;

    SyncRequestExecutor() {
        DruidHttpConfig initializationConfig = DruidHttp.getInitializeConfig();
        mRequestHandler = new RequestHandler(

        );
    }
    /**
     * Perform a handle.
     */
    public  DruidHttpResponse execute(DruidHttpRequest request) {
        return mRequestHandler.handle(request);
    }
}
