package com.druid.http.able;


import com.druid.http.OkHttpCallBack;

public interface Callable {
    void cancel();

    void execute(OkHttpCallBack callBack);
}
