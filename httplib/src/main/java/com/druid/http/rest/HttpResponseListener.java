package com.druid.http.rest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.druid.http.DruidHttp;
import com.druid.http.DruidHttpHeaders;
import com.druid.http.DruidHttpLogger;
import com.druid.http.HttpListener;
import com.druid.http.HttpReqDialog;
import com.druid.http.R;
import com.druid.http.loading.HttpsLoadingDialog;

import java.util.List;

public class HttpResponseListener implements OnResponseListener {

    private Context mContext;
    /**
     * Dialog.
     */
    private HttpsLoadingDialog mWaitDialog;
    /**
     * Request.
     */
    private DruidHttpRequest mRequest;
    /**
     * 结果回调.
     */
    private HttpListener callback;

    private int what;

    /**
     * @param context      context用来实例化dialog.
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     * @param canCancel    是否允许用户取消请求.
     * @param isLoading    是否显示dialog.
     */
    public HttpResponseListener(Context context, DruidHttpRequest request, HttpListener httpCallback,
                                int what, boolean canCancel, boolean isLoading) {
        this.mContext = context;
        this.what = what;
        this.mRequest = request;
        if (context != null && isLoading) {
            mWaitDialog = request.getLoadingDialog(context, context.getResources().getString(R.string.loading_dot));
            mWaitDialog.setDialogCancelable(canCancel);
            mWaitDialog.setDialogOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mRequest.cancel();
                }
            });
        }
        this.callback = httpCallback;
    }

    public HttpResponseListener(Context context, DruidHttpRequest request, HttpListener httpCallback,
                                int what, boolean canCancel, boolean isLoading, String message) {
        this.mContext = context;
        this.what = what;
        this.mRequest = request;
        if (context != null && isLoading) {
            mWaitDialog = request.getLoadingDialog(context, message);
            mWaitDialog.setDialogCancelable(canCancel);
            mWaitDialog.setDialogOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mRequest.cancel();
                }
            });
        }
        this.callback = httpCallback;
    }

    /**
     * 开始请求, 这里显示一个dialog.
     */
    @Override
    public void onStart(int what) {
        DruidHttpLogger.iContent(mRequest.getCancelTag(), "url:" +mRequest.getUrl() + "-->time-http-onStart:" + System.currentTimeMillis());

        if (mWaitDialog != null && !mWaitDialog.isDialogShowing()) {
            mWaitDialog.dialogShow();
        }
    }

    @Override
    public void onSucceed(int what, DruidHttpResponse response) {
        DruidHttpLogger.iContent(response.getCancelTag(), "url:" + response.getRequest().getUrl() + "-->time-http-callback:" + System.currentTimeMillis());
        int responseCode = response.code();
        //
        if (responseCode == 401) {
            if (response.getHeaders() != null) {
                if (!response.getHeaders().containsKey(DruidHttpHeaders.IGNORE_TOKEN_KEY)) {
                    if (DruidHttp.getInitializeConfig().getUnauthorizedListener() != null) {
                        DruidHttp.getInitializeConfig().getUnauthorizedListener().unauthorized(response);
                    }
                    return;
                }
            }
        }
        success(response);
    }

    private void success(DruidHttpResponse response) {
        boolean hasCallBack = false;
        if (mContext != null) {
            try {
                if (mContext instanceof Activity) {
                    Activity activity = (Activity) mContext;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onSucceed(what, response);
                            }
                        }
                    });
                    hasCallBack = true;
                }
            } catch (Exception ex) {

            }
        }
        if (hasCallBack == false) {
            if (callback != null) {
                callback.onSucceed(what, response);
            }
        }
    }

    /**
     * 失败回调.
     */
    @Override
    public void onFailed(int what, DruidHttpResponse response) {
        DruidHttpLogger.iContent(response.getCancelTag(), "url:" + response.getRequest().getUrl() + "-->time-http-callback:" + System.currentTimeMillis());

        failed(response);
    }

    private void failed(DruidHttpResponse response) {
        boolean hasCallBack = false;
        if (mContext != null) {
            try {
                if (mContext instanceof Activity) {
                    Activity activity = (Activity) mContext;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFailed(what, response);
                            }
                        }
                    });
                    hasCallBack = true;
                }
            } catch (Exception ex) {

            }
        }
        if (hasCallBack == false) {
            if (callback != null) {
                callback.onFailed(what, response);
            }
        }
    }

    /**
     * 结束请求, 这里关闭dialog.
     */
    @Override
    public void onFinish(int what) {
        DruidHttpLogger.iContent(mRequest.getCancelTag(), "url:" +mRequest.getUrl() + "-->time-http-onFinish:" + System.currentTimeMillis());
        if (mWaitDialog != null && mWaitDialog.isDialogShowing()) {
            mWaitDialog.dialogStop();
        }
    }
}
