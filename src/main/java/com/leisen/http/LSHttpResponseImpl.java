package com.leisen.http;

import org.apache.mina.http.api.DefaultHttpResponse;
import org.apache.mina.http.api.HttpResponse;
import org.apache.mina.http.api.HttpStatus;
import org.apache.mina.http.api.HttpVersion;

import java.util.Map;

/**
 * http响应，继承DefaultHttpResponse类，为了添加body属性的封装
 */
public class LSHttpResponseImpl extends DefaultHttpResponse {
    private String body;

    /**
     * 构造器
     * @param version
     * @param status
     * @param headers
     * @param body
     */
    public LSHttpResponseImpl(HttpVersion version, HttpStatus status, Map<String, String> headers, String body) {
        super(version, status, headers);
        this.body = body;
    }

    /**
     * body属性的getter方法
     * @return
     */
    public String getBody() {
        return body;
    }
}
