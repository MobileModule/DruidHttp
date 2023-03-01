package com.druid.http.rest;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.druid.http.DruidHttp;
import com.druid.http.DruidHttpHeaders;
import com.druid.http.HttpJSONBody;
import com.druid.http.HttpReqDialog;
import com.druid.http.Params;
import com.druid.http.Priority;
import com.druid.http.RequestMethod;
import com.druid.http.able.Callable;
import com.druid.http.able.Cancelable;
import com.druid.http.able.Finishable;
import com.druid.http.able.Startable;
import com.druid.http.loading.HttpsLoadingDialog;
import com.druid.http.tools.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public abstract class BaseHttpRequest implements Comparable<BaseHttpRequest>, Callable, Startable, Cancelable, Finishable {
    public BaseHttpRequest(String url, RequestMethod requestMethod) {
        this.url = url;
        mRequestMethod = requestMethod;
        mHeaders = new DruidHttpHeaders();
        if (!TextUtils.isEmpty(mUserAgent)) {
            mHeaders.set(DruidHttpHeaders.HEAD_KEY_USER_AGENT, mUserAgent);
        }
        MultiValueMap<String, String> globalHeaders = DruidHttp.getInitializeConfig().getHeaders();
        for (Map.Entry<String, List<String>> headersEntry : globalHeaders.entrySet()) {
            String key = headersEntry.getKey();
            List<String> valueList = headersEntry.getValue();
            for (String value : valueList) {
                mHeaders.add(key, value);
            }
        }
        mParams = new Params();
        MultiValueMap<String, String> globalParams = DruidHttp.getInitializeConfig().getParams();
        for (Map.Entry<String, List<String>> paramsEntry : globalParams.entrySet()) {
            List<String> valueList = paramsEntry.getValue();
            for (String value : valueList) {
                mParams.add(paramsEntry.getKey(), value);
            }
        }
    }

    public BaseHttpRequest(String url, RequestMethod requestMethod, String jsonBodyContent) {
        this(url, requestMethod);
        this.jsonBodyContent = jsonBodyContent;
    }

    public Request getRequestBuilder() {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);
        //header
        setRequestHeader(requestBuilder);
        //content
        if (mRequestMethod == null) {
            requestBuilder.get();
        } else {
            if (mRequestMethod == RequestMethod.POST) {
                if (TextUtils.isEmpty(jsonBodyContent)) {
                    requestBuilder.post(HttpJSONBody.getEmptyBody());
                } else {
                    requestBuilder.post(HttpJSONBody.getJSONBody(jsonBodyContent));
                }
            }
            if (mRequestMethod == RequestMethod.DELETE) {
                if (TextUtils.isEmpty(jsonBodyContent)) {
                    requestBuilder.delete();
                } else {
                    requestBuilder.delete(HttpJSONBody.getJSONBody(jsonBodyContent));
                }
            }
            if (mRequestMethod == RequestMethod.PUT) {
                if (TextUtils.isEmpty(jsonBodyContent)) {
                    requestBuilder.put(HttpJSONBody.getEmptyBody());
                } else {
                    requestBuilder.put(HttpJSONBody.getJSONBody(jsonBodyContent));
                }
            }
        }
        return requestBuilder.build();
    }

    private void setRequestHeader(Request.Builder requestBuilder) {
        for (Map.Entry<String, List<String>> headersEntry : mHeaders.entrySet()) {
            String key = headersEntry.getKey();
            List<String> valueList = headersEntry.getValue();
            for (String value : valueList) {
                requestBuilder.addHeader(key, value);
            }
        }
    }

    protected OkHttpClient getHttpClient() {
        return new OkHttpClient.Builder()
                .sslSocketFactory(getSSLSocketFactory())
                .followRedirects(getFollowRedirects())
                .hostnameVerifier(getHostnameVerifier())
                .connectTimeout(mConnectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(mReadTimeout, TimeUnit.MILLISECONDS)
                .build();
    }

    private Call requestCall = null;

    public void setRequestCall(Call call) {
        this.requestCall = call;
    }

    public Call getRequestCall() {
        return requestCall;
    }

    /**
     * Request priority.
     */
    private Priority mPriority = Priority.DEFAULT;

    public void setPriority(Priority priority) {
        this.mPriority = priority;
    }

    public Priority getPriority() {
        return mPriority;
    }

    /**
     * The sequence.
     */
    private int sequence;

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSequence() {
        return this.sequence;
    }

    /**
     * The JsonBodyContent.
     */
    private String jsonBodyContent = "";

    public void setJsonBodyContent(String mJsonBodyContent) {
        this.jsonBodyContent = mJsonBodyContent;
    }

    public String getJsonBodyContent() {
        return jsonBodyContent;
    }

    /**
     * Target address.
     */
    private String url;

    public String getUrl() {
        return url;
    }

    /**
     * Request method.
     */
    private RequestMethod mRequestMethod;

    public RequestMethod getRequestMethod() {
        return mRequestMethod;
    }

    /**
     * SSLSockets.
     */
    private SSLSocketFactory mSSLSocketFactory = DruidHttp.getInitializeConfig().getSSLSocketFactory();

    public void setSSLSocketFactory(SSLSocketFactory socketFactory) {
        mSSLSocketFactory = socketFactory;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return mSSLSocketFactory;
    }

    /**
     * HostnameVerifier.
     */
    private HostnameVerifier mHostnameVerifier = DruidHttp.getInitializeConfig().getHostnameVerifier();

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        mHostnameVerifier = hostnameVerifier;
    }

    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }

    /**
     * Connect timeout of handle.
     */
    private int mConnectTimeout = DruidHttp.getInitializeConfig().getConnectTimeout();

    public void setConnectTimeout(int connectTimeout) {
        mConnectTimeout = connectTimeout;
    }

    public int getConnectTimeout() {
        return mConnectTimeout;
    }

    /**
     * Read data timeout.
     */
    private int mReadTimeout = DruidHttp.getInitializeConfig().getReadTimeout();

    public void setReadTimeout(int readTimeout) {
        mReadTimeout = readTimeout;
    }

    public int getReadTimeout() {
        return mReadTimeout;
    }

    private boolean mFollowRedirects = DruidHttp.getInitializeConfig().getFollowRedirects();

    public void setFollowRedirects(boolean followRedirects) {
        this.mFollowRedirects = followRedirects;
    }

    public boolean getFollowRedirects() {
        return this.mFollowRedirects;
    }

    /**
     * After the failure of retries.
     */
    private int mRetryCount = DruidHttp.getInitializeConfig().getRetryCount();

    public void setRetryCount(int count) {
        this.mRetryCount = count;
    }

    public int getRetryCount() {
        return mRetryCount;
    }

    private String mUserAgent = DruidHttp.getInitializeConfig().getUserAgent();

    /**
     * Request heads.
     */
    private DruidHttpHeaders mHeaders;

    public void addHeader(String key, String value) {
        mHeaders.add(key, value);
    }

    public void setHeader(String key, String value) {
        mHeaders.set(key, value);
    }

    /**
     * Param collection.
     */
    private Params mParams;

    public MultiValueMap<String, Object> getParamKeyValues() {
        return mParams;
    }

    /**
     * The record has started.
     */
    private boolean isStart = false;

    /**
     * The handle is completed.
     */
    private boolean isFinished = false;

    /**
     * Has been canceled.
     */
    private boolean isCanceled = false;

    /**
     * Cancel sign.
     */
    private Object mCancelSign;

    public void setCancelSign(Object sign) {
        this.mCancelSign = sign;
    }

    public void cancelBySign(Object sign) {
        if (mCancelSign == sign || (sign != null && mCancelSign != null && mCancelSign.equals(sign)))
            cancel();
    }

    public Object getCancelSign() {
        return mCancelSign;
    }

    /**
     * Tag of handle.
     */
    private Object mTag;

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    public Object getTag() {
        return this.mTag;
    }

    @Override
    public void finish() {
        this.isFinished = true;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void start() {
        this.isStart = true;
    }

    @Override
    public boolean isStarted() {
        return isStart;
    }

    @Override
    public void cancelHand() {
        if (!isCanceled) {
            isCanceled = true;
            //cancel
        }
    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }

    @Override
    public final int compareTo(BaseHttpRequest another) {
        final Priority me = getPriority();
        final Priority it = another.getPriority();
        return me == it ? getSequence() - another.getSequence() : it.ordinal() - me.ordinal();
    }

    private HttpsLoadingDialog loadingDialog = null;

    public HttpsLoadingDialog getLoadingDialog(Context context, String message) {
        if (loadingDialog == null) {
            loadingDialog = new HttpReqDialog(context, message);
        }
        loadingDialog.setLoadingTips(message);
        return loadingDialog;
    }

    public void setLoadingDialog(HttpsLoadingDialog dialog) {
        this.loadingDialog = dialog;
    }
}
