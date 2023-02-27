package com.druid.http;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class HttpJSONBody {
    public static RequestBody getJSONBody(String jsonStr) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, jsonStr);
        return body;
    }

    public static RequestBody getEmptyBody() {
        return new FormBody.Builder().build();
    }

}
