package com.druid.http.able;


import okhttp3.Response;

public interface Callable {
    void cancel();

    Response execute();
}
