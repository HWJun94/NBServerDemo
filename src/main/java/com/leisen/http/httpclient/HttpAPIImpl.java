package com.leisen.http.httpclient;
import java.util.HashMap;
import java.util.Map;

import com.leisen.util.JSONUtil;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.fasterxml.jackson.databind.ObjectMapper;

//import org.omg.CORBA.PUBLIC_MEMBER;

public class HttpAPIImpl {
    private static String accessToken;
    private static String refreshToken;
    private static Integer expiresIn;
    private static String myip="https://180.101.147.89:8743/iocm/app/sec/v1.1.0/";
    public static String appId="WHVj6DprSAffZwPhhYoxyX0SLLAa";
    public static String secret="40LHovn2rqBTTiyD5bFXmdro11sa";
    public static String deviceId="c179c1c7-a8ff-48a7-9a74-1735c369ad3a";
    public static String serviceId="DataRX";
    public static String COMMAND_STRING="12345678900987654111";
    public void Auth() throws Exception {
        String url =myip+ "login";
        String charset = "utf-8";
        //String httpOrgCreateTest = url;
        Map<String,String> createMap = new HashMap<String,String>();
        createMap.put("appId",appId);
        createMap.put("secret",secret);
        HttpClientUtil httpClientUtil= new HttpClientUtil();
        String httpOrgCreateTestRtn = httpClientUtil.doPost(url,createMap,charset);
        System.out.println("result:"+httpOrgCreateTestRtn);
//        Map<String, Object> map = JsonToMap.toMap(httpOrgCreateTestRtn);
        Map map = (Map)JSONUtil.JSON2Object(httpOrgCreateTestRtn, Map.class);
        accessToken = (String) map.get("accessToken");
        refreshToken= (String) map.get("refreshToken");
        expiresIn= (Integer) map.get("expiresIn");
    }
    //	public static void main(String[] args) {
//		HttpAPIImpl httpAPIImpl = new HttpAPIImpl();
//		httpAPIImpl.Auth();
//		System.out.println(accessToken);
//
//
//	}
    public void  RefreshToken() throws Exception{
        String url=myip+"refreshToken";
        Map<String, String> map=new HashMap<>();
        map.put("appId",appId );
        map.put("secret",secret);
        map.put("refreshToken",refreshToken);
//        String jason=MapToJaon.MapToJason(map);
        String jason = JSONUtil.object2JSON(map);
        HttpClientUtil httpClientUtil=new HttpClientUtil();
        String response = httpClientUtil.doPostForJson(url, jason,null);
        System.out.println("respnse:"+response);
//        Map<String, Object> map01 = JsonToMap.toMap(response);
        Map map01 = (Map) JSONUtil.JSON2Object(response, Map.class);
        accessToken = (String) map01.get("accessToken");
        refreshToken= (String) map01.get("refreshToken");
        expiresIn= (Integer) map01.get("expiresIn");
    }
    //public static void main(String[] args) {
//	HttpAPIImpl httpAPIImpl= new HttpAPIImpl();
//	httpAPIImpl.Auth();
//	httpAPIImpl.RefreshToken();
//	//System.out.println(accessToken);
//}
    public void subscribe(String notifyType) throws Exception{
        String url = "https://180.101.147.89:8743/iocm/app/sub/v1.2.0/subscribe";
        Map< String, String> map = new HashMap<>();
        map.put("notifyType", notifyType);
        map.put("callbackurl", "https://182.61.13.156:8443");
        HttpClientUtil httpClientUtil=new HttpClientUtil();
        Header[] headers= new Header[3];
        headers[0]=new BasicHeader("notifyType",notifyType );
        headers[1]=new BasicHeader("Authorization","bearer "+accessToken);
        headers[2]= new BasicHeader("app_key", appId);
        ObjectMapper mapper = new ObjectMapper();
        String jason = mapper.writeValueAsString(map);
//		String jason=MapToJaon.MapToJason(map);
        System.out.println(jason);
        String response=httpClientUtil.doPostForJson(url, jason, headers);
//		System.out.println(response);

    }
    //	public static void main(String[] args) throws Exception{
//		HttpAPIImpl httpAPIImpl = new HttpAPIImpl();
//		httpAPIImpl.Auth();
//		httpAPIImpl.subscribe("deviceInfoChanged");
//	}
    public void deviceCommands(String deviceId, String message) throws Exception{
        String url = "https://180.101.147.89:8743/iocm/app/cmd/v1.4.0/deviceCommands";
        HttpClientUtil httpClientUtil=new HttpClientUtil();
        Header[] headers= new Header[3];
        headers[0]=new BasicHeader("app_key",appId);
        headers[1]=new BasicHeader("Authorization","bearer "+accessToken);
        headers[2]=new BasicHeader("Content-Type","application/json");
        ObjectMapper mapper  = new ObjectMapper();
//		mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, false);
        Map map01= new HashMap<>();
        map01.put("serviceId",serviceId );
        map01.put("method", "COMMAND_RX");
//		String s1= mapper.writeValueAsString(map01);
        Map<String, String> map02=new HashMap<>();
        map02.put("dataString",message);
        map01.put("paras", map02);
        //String s2= mapper.writeValueAsString(map02);
        Map map03=new HashMap<>();
        map03.put("command", map01);
        map03.put("deviceId", deviceId);
//		String Json=MapToJaon.MapToJason(map03);

        String Json= mapper.writeValueAsString(map03);
        System.out.println(Json);
        String response=httpClientUtil.doPostForJson(url, Json, headers);
        System.out.println(response);
    }
//
//    public static void main(String[] args) throws Exception {
//        HttpAPIImpl httpAPIImpl = new HttpAPIImpl();
//        httpAPIImpl.Auth();
//        httpAPIImpl.deviceCommands();
//    }
}

