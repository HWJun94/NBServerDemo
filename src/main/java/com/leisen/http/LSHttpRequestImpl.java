package com.leisen.http;

import org.apache.mina.http.HttpRequestImpl;
import org.apache.mina.http.api.HttpMethod;
import org.apache.mina.http.api.HttpRequest;
import org.apache.mina.http.api.HttpVersion;

import java.util.Map;

/**
 * Http请求 继承HttpRequestImpl类，为了添加body属性的封装
 */
public class LSHttpRequestImpl extends HttpRequestImpl {

    private final String body;

    /**
     * 构造器
     * @param version
     * @param method
     * @param requestedPath
     * @param queryString
     * @param headers
     * @param body
     */
    public LSHttpRequestImpl(HttpVersion version, HttpMethod method, String requestedPath, String queryString, Map<String, String> headers, String body) {
        super(version, method, requestedPath, queryString, headers);
        this.body = body;
    }

    /**
     * body属性的getter方法
     * @return
     */
    public String getBody() {
        return  body.equals("") ? null : body;
    }
}
