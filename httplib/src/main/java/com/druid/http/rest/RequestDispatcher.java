package com.druid.http.rest;

import android.os.Process;

import com.druid.http.DruidHttpLogger;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class RequestDispatcher extends Thread {

    private final BlockingQueue<DruidHttpRequest> mRequestQueue;
    private final BlockingQueue<DruidHttpRequest> mUnFinishQueue;
    private final Map<DruidHttpRequest, Messenger> mMessengerMap;

    /**
     * Whether the current handle queue polling thread is out of.
     */
    private volatile boolean mQuit = false;

    public RequestDispatcher(BlockingQueue<DruidHttpRequest> requestQueue,BlockingQueue<DruidHttpRequest> unFinishQueue,
                             Map<DruidHttpRequest, Messenger> messengerMap) {
        this.mRequestQueue = requestQueue;
        this.mUnFinishQueue = unFinishQueue;
        this.mMessengerMap = messengerMap;
    }

    /**
     * Exit polling thread.
     */
    public void quit() {
        this.mQuit = true;
        interrupt();
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (!mQuit) {
            final DruidHttpRequest request;
            try {
                request = mRequestQueue.take();
            } catch (InterruptedException e) {
                if (mQuit) {
                    DruidHttpLogger.w("Queue exit, stop blocking.");
                    break;
                }
                DruidHttpLogger.e(e);
                continue;
            }

            if (request.isCanceled()) {
                mRequestQueue.remove(request);
                mUnFinishQueue.remove(request);
                mMessengerMap.remove(request);
                DruidHttpLogger.d(request.getUrl() + " is canceled.");
                continue;
            }

            try {
                // start.
                if(request!=null) {
                    request.start();
                    mMessengerMap.get(request).start();

                    // handle.
                    DruidHttpResponse response = SyncRequestExecutor.INSTANCE.execute(request);

                    // response.
                    if (request.isCanceled()) {
                        DruidHttpLogger.d(request.getUrl() + " finish, but it's canceled.");
                    } else {
                        // noinspection unchecked
                        mMessengerMap.get(request).response(response);
                    }

                    // finish.
                    request.finish();
                    mMessengerMap.get(request).finish();

                    // remove it from queue.
                    mRequestQueue.remove(request);
                    mUnFinishQueue.remove(request);
                    mMessengerMap.remove(request);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
