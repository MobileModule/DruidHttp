package com.druid.http.lifecycle;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public class NetLifecycleObserver implements LifecycleObserver {
    private String mTag = "";
    private final NetLifecycleObserverCallback mCallback;
    private final LifecycleOwner mLifecycleOwner;

    public NetLifecycleObserver(LifecycleOwner lifecycleOwner, NetLifecycleObserverCallback callback, String tag) {
        mTag = tag;
        mLifecycleOwner = lifecycleOwner;
        mCallback = callback;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        mCallback.onStart(mLifecycleOwner, mTag);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        mCallback.onStop(mLifecycleOwner, mTag);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        mCallback.onDestroy(mLifecycleOwner, mTag);
    }
}
