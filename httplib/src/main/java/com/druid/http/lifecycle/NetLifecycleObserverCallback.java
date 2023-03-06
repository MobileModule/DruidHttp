package com.druid.http.lifecycle;

import androidx.lifecycle.LifecycleOwner;

public interface NetLifecycleObserverCallback {
    void onStop(LifecycleOwner owner,String tag);

    void onStart(LifecycleOwner owner,String tag);

    void onDestroy(LifecycleOwner owner,String tag);
}
