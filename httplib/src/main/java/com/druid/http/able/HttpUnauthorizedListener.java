package com.druid.http.able;

import com.druid.http.rest.DruidHttpResponse;

public interface HttpUnauthorizedListener {
    void unauthorized(DruidHttpResponse response);
}
