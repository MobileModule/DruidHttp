package com.druid.http.rest;

import com.druid.http.DruidHttpHeaders;
import com.druid.http.DruidHttpLogger;
import com.druid.http.HttpCode;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Response;

public class RequestHandler {
    public static final String TAG = RequestHandler.class.getName();

    public RequestHandler() {

    }

    public DruidHttpResponse handle(DruidHttpRequest request) {
        long start_time = System.currentTimeMillis();
        DruidHttpLogger.iContent(request.getCancelTag(), "url:" + request.getUrl() + "-->time-http-start:" + start_time);
        Response response = request.execute();//同步请求
        long end_time = System.currentTimeMillis();
        DruidHttpLogger.iContent(request.getCancelTag(), "url:" + request.getUrl() + "-->time-http-end:" + end_time + "-->time-diff:" + (end_time - start_time));
        final DruidHttpResponse httpResponse = new DruidHttpResponse();
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

}
