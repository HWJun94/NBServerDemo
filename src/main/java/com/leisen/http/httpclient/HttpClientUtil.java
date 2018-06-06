package com.leisen.http.httpclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.Header;
/*
 * 利用HttpClient进行post请求的工具类
 */
public class HttpClientUtil {
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 7000;
    public HttpClient httpClient;

    public HttpClientUtil() {
        InitSSLconfig ssl= new InitSSLconfig();
        try {
            ssl.initSSLConfigForTwoWay();
            httpClient = InitSSLconfig.httpClient;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }
    public  String doPost(String url,Map<String,String> map,String charset){
//        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            //httpClient = new SSLClient();
//        	InitSSLconfig ssl= new InitSSLconfig();
//        	ssl.initSSLConfigForTwoWay();
//        	httpClient=InitSSLconfig.httpClient;
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Entry<String,String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }
            if(list.size() > 0){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                    System.out.println(result);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }


    public  String doPostForJson(String url, Object json,Header[] headers){
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        //String result = null;
        HttpResponse response = null;
        String httpstr = null;
        try {
            InitSSLconfig ssl= new InitSSLconfig();
            ssl.initSSLConfigForTwoWay();
            httpClient=InitSSLconfig.httpClient;
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(),"UTF-8");//解决中文乱码问题
            if (headers!=null) {
//				httpPost.setHeaders(headers);
                //httpPost.setHeaders(headers);
                httpPost.setHeaders(headers);
            }
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response =  httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    httpstr = EntityUtils.toString(resEntity,"utf-8");
                    System.out.println(httpstr);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return httpstr;
    }
}