package com.druid.http;

import android.content.Context;

import com.druid.http.able.HttpUnauthorizedListener;
import com.druid.http.tools.LinkedMultiValueMap;
import com.druid.http.tools.MultiValueMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

public final class DruidHttpConfig {

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    private Context mContext;

    private int mConnectTimeout;
    private int mReadTimeout;

    private int mRetryCount;
    private SSLSocketFactory mSSLSocketFactory;
    private HostnameVerifier mHostnameVerifier;
    private HttpUnauthorizedListener unauthorizedListener;
    private boolean mFollowRedirects;//全局支持重定向

    private MultiValueMap<String, String> mHeaders;
    private MultiValueMap<String, String> mParams;

    private String mUserAgent = "";

    private DruidHttpConfig(Builder builder) {
        this.mContext = builder.mContext;

        this.mConnectTimeout = builder.mConnectTimeout;
        this.mReadTimeout = builder.mReadTimeout;

        this.mRetryCount = builder.mRetryCount;
        this.mSSLSocketFactory = builder.mSSLSocketFactory;
        if (this.mSSLSocketFactory == null)
            this.mSSLSocketFactory = SSLSocketClient.getSSLSocketFactory();
        this.mHostnameVerifier = builder.mHostnameVerifier;
        if (this.mHostnameVerifier == null)
            this.mHostnameVerifier = SSLSocketClient.getHostnameVerifier();

        this.mHeaders = builder.mHeaders;
        this.mParams = builder.mParams;
        this.mUserAgent = builder.mUserAgent;
        this.unauthorizedListener = builder.mUnauthorizedListener;
        this.mFollowRedirects = builder.followRedirects;
    }

    public Context getContext() {
        return mContext;
    }

    public int getConnectTimeout() {
        return mConnectTimeout;
    }

    public int getReadTimeout() {
        return mReadTimeout;
    }

    public int getRetryCount() {
        return mRetryCount;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return mSSLSocketFactory;
    }

    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }

    public MultiValueMap<String, String> getHeaders() {
        return mHeaders;
    }

    public MultiValueMap<String, String> getParams() {
        return mParams;
    }

    public String getUserAgent() {
        return mUserAgent;
    }

    public HttpUnauthorizedListener getUnauthorizedListener() {
        return unauthorizedListener;
    }

    public boolean getFollowRedirects() {
        return mFollowRedirects;
    }

    public final static class Builder {

        private Context mContext;

        private int mConnectTimeout = 10 * 1000;
        private int mReadTimeout = 10 * 1000;

        private int mRetryCount;
        private SSLSocketFactory mSSLSocketFactory;
        private HostnameVerifier mHostnameVerifier;

        private MultiValueMap<String, String> mHeaders = new LinkedMultiValueMap<>();
        private MultiValueMap<String, String> mParams = new LinkedMultiValueMap<>();


        private String mUserAgent = "";

        private HttpUnauthorizedListener mUnauthorizedListener;

        private boolean followRedirects;

        private Builder(Context context) {
            this.mContext = context.getApplicationContext();
        }

        /**
         * Connection timed out.
         *
         * @param millis millisecond.
         */
        public Builder connectionTimeout(int millis) {
            this.mConnectTimeout = millis;
            return this;
        }

        /**
         * Read the server's data timeout.
         *
         * @param millis millisecond.
         */
        public Builder readTimeout(int millis) {
            this.mReadTimeout = millis;
            return this;
        }

        /**
         * Retry count.
         */
        public Builder retry(int count) {
            this.mRetryCount = count;
            return this;
        }

        /**
         * Global SSLSocketFactory.
         */
        public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.mSSLSocketFactory = sslSocketFactory;
            return this;
        }

        /**
         * Global HostnameVerifier.
         */
        public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.mHostnameVerifier = hostnameVerifier;
            return this;
        }

        /**
         * Add the global handle header.
         */
        public Builder addHeader(String key, String value) {
            mHeaders.add(key, value);
            return this;
        }

        /**
         * Add the global handle param.
         */
        public Builder addParam(String key, String value) {
            mParams.add(key, value);
            return this;
        }

        public Builder setUserAgent(String userAgent) {
            this.mUserAgent = userAgent;
            return this;
        }

        public Builder setUnauthorizedListener(HttpUnauthorizedListener listener) {
            this.mUnauthorizedListener = listener;
            return this;
        }

        public Builder setFollowRedirects(boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }

        public DruidHttpConfig build() {
            return new DruidHttpConfig(this);
        }
    }

}
