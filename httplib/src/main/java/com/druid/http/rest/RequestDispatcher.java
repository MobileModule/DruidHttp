package com.druid.http.rest;

import com.druid.http.DruidHttpHeaders;
import com.druid.http.DruidHttpLogger;
import com.druid.http.HttpCode;
import com.druid.http.OkHttpCallBack;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

public class RequestDispatcher implements OkHttpCallBack {

    private int what = 0;
    private DruidHttpRequest request;
    private OnResponseListener listener;
    private MessagePoster messagePoster;

    public RequestDispatcher(int what, DruidHttpRequest request,
                             OnResponseListener listener, MessagePoster messagePoster) {
        this.what = what;
        this.request = request;
        this.listener = listener;
        this.messagePoster = messagePoster;
    }

    public void run() {
        if (request.isCanceled()) {
            messagePoster.remove(request);
            DruidHttpLogger.d(request.getUrl() + " is canceled.");
        }

        try {
            // start.
            if (request != null) {
                request.start();
                listener.onStart(what);
                // handle.
                request.execute(this);//异步请求
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        DruidHttpResponse httpResponse = handleFailedData(e.getMessage());
        handleResult(httpResponse);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        DruidHttpResponse httpResponse = handleSuccessData(response);
        handleResult(httpResponse);
    }

    private DruidHttpResponse handleSuccessData(Response response) {
        DruidHttpResponse httpResponse = new DruidHttpResponse();
        try {
            if (response != null) {
                int responseCode = response.code();
                String bodyContent = response.body().string();
                httpResponse.setCode(responseCode);
                httpResponse.setSucceed(HttpCode.requestSuccess(responseCode));
                httpResponse.setContent(bodyContent);
                httpResponse.setHeaders(parseHeader(response.headers()));
            } else {
                httpResponse.setCode(HttpCode.HTTP_LIB_ERROR);
                httpResponse.setContent("response nil");
            }
        } catch (Exception ex) {
            httpResponse.setCode(HttpCode.HTTP_LIB_ERROR);
            httpResponse.setContent("response " + ex.getMessage());
        }
        httpResponse.setRequest(request);
        DruidHttpLogger.iContent(DruidHttpLogger.class.getName(), httpResponse.toString());
        return httpResponse;
    }

    private DruidHttpHeaders parseHeader(Headers headers) {
        DruidHttpHeaders httpHeaders = new DruidHttpHeaders();
        if (headers != null) {
            Map<String, List<String>> headerMaps = headers.toMultimap();
            Iterator<Map.Entry<String, List<String>>> it = headerMaps.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<String>> entry = it.next();
                httpHeaders.set(entry.getKey(), entry.getValue());
            }
        }
        return httpHeaders;
    }

    private DruidHttpResponse handleFailedData(String errorMessage) {
        DruidHttpResponse httpResponse = new DruidHttpResponse();
        httpResponse.setCode(HttpCode.HTTP_LIB_ERROR);
        httpResponse.setContent(errorMessage);
        httpResponse.setRequest(request);
        return httpResponse;
    }

    private void handleResult(DruidHttpResponse response) {
        // response.
        if (request.isCanceled()) {
            DruidHttpLogger.d(request.getUrl() + " finish, but it's canceled.");
        } else {
            // noinspection unchecked
            if (response.isSucceed()) {
                listener.onSucceed(what, response);
            } else {
                listener.onFailed(what, response);
            }
        }

        // finish.
        request.finish();
        listener.onFinish(what);
        // remove it from queue.
        messagePoster.remove(request);
    }
}
