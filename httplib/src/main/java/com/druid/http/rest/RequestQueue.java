package com.druid.http.rest;

import com.druid.http.DruidHttpLogger;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestQueue implements MessagePoster {
    private AtomicInteger mInteger = new AtomicInteger();
    private final Map<DruidHttpRequest, RequestDispatcher> mDispatchers = new LinkedHashMap<>();

    public RequestQueue(int threadPoolSize) {

    }

    public void start() {

    }

    public void add(int what, DruidHttpRequest request, OnResponseListener listener) {
        request.setSequence(mInteger.incrementAndGet());
        RequestDispatcher networkDispatcher = new RequestDispatcher(what, request,
                listener, this);
        mDispatchers.put(request, networkDispatcher);
        networkDispatcher.run();
    }

    @Override
    public void remove(DruidHttpRequest request) {
        synchronized (mDispatchers) {
            Iterator<Map.Entry<DruidHttpRequest, RequestDispatcher>> its = mDispatchers.entrySet().iterator();
            while (its.hasNext()) {
                Map.Entry<DruidHttpRequest, RequestDispatcher> entry = its.next();
                DruidHttpRequest requestKey = entry.getKey();
                if (requestKey.getSequence() == request.getSequence()) {
                    its.remove();
                    DruidHttpLogger.i("tag:" + "【" + request.getCancelSign() + "】" + ",url【" + request.getUrl() + "】," +
                            "finish remove request seq " + request.getSequence());
                }
            }
        }
    }


    public void cancelBySign(Object sign) {
        synchronized (mDispatchers) {
            Iterator<Map.Entry<DruidHttpRequest, RequestDispatcher>> its = mDispatchers.entrySet().iterator();
            while (its.hasNext()) {
                Map.Entry<DruidHttpRequest, RequestDispatcher> entry = its.next();
                DruidHttpRequest request = entry.getKey();
                request.cancelBySign(sign);
                its.remove();
                DruidHttpLogger.i("tag:" + "【" + request.getCancelSign() + "】" + ",url【" + request.getUrl() + "】," +
                        "cancel sign remove request seq " + request.getSequence());
            }
        }
    }

    public void cancelAll() {
        synchronized (mDispatchers) {
            Iterator<Map.Entry<DruidHttpRequest, RequestDispatcher>> its = mDispatchers.entrySet().iterator();
            while (its.hasNext()) {
                Map.Entry<DruidHttpRequest, RequestDispatcher> entry = its.next();
                DruidHttpRequest request = entry.getKey();
                request.cancel();
                its.remove();
                DruidHttpLogger.i("tag:" + "【" + request.getCancelSign() + "】" + ",url【" + request.getUrl() + "】," +
                        "cancel all remove request seq " + request.getSequence());
            }
        }
    }
}
