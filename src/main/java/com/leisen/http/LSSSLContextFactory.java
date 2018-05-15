package com.leisen.http;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

public class LSSSLContextFactory {
    private static final String KEYSTORE_PWD = "123456";

    private static final String KEYSTORE = "/keystore.jks";
    private static final String TRUSTSTORE = "/truststore.jks";

    public static SSLContext getInstance() throws Exception {

        //获取服务器根证书
        KeyStore keyStore = KeyStore.getInstance("JKS");
        InputStream kSIn = LSSSLContextFactory.class.getResourceAsStream(KEYSTORE);
        keyStore.load(kSIn, KEYSTORE_PWD.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
        kmf.init(keyStore, KEYSTORE_PWD.toCharArray());

        //获取信任列表
        KeyStore trustStore = KeyStore.getInstance("JKS");
        InputStream tsIn = LSSSLContextFactory.class.getResourceAsStream(TRUSTSTORE);
        trustStore.load(tsIn, KEYSTORE_PWD.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
        tmf.init(trustStore);

        //创建SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return sslContext;
    }
}
