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

    private static final String CLIENT_KEYSTORE = "/outgoing.CertwithKey.pkcs12";
    private static final String CLIENT_TRUSTSTORE = "/ca.jks";
    private static final String CLIENT_KEYSTORE_PWD = "IoM@1234";
    private static final String CLIENT_TRUSTSTORE_PWD = "Huawei@123";

    private boolean serverMod;

    public static SSLContext getInstance(boolean serverMod) throws Exception {

        //获取服务器根证书
        KeyStore keyStore = KeyStore.getInstance(serverMod ? "JKS" : "pkcs12");
        InputStream kSIn = LSSSLContextFactory.class.getResourceAsStream(serverMod ? KEYSTORE : CLIENT_KEYSTORE);
        String keystorePwd = serverMod ? KEYSTORE_PWD : CLIENT_KEYSTORE_PWD;
        keyStore.load(kSIn, keystorePwd.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, keystorePwd.toCharArray());

        //获取信任列表
        KeyStore trustStore = KeyStore.getInstance("JKS");
        InputStream tsIn = LSSSLContextFactory.class.getResourceAsStream(serverMod ? TRUSTSTORE : CLIENT_TRUSTSTORE);
        String truststorePwd = serverMod ? KEYSTORE_PWD : CLIENT_TRUSTSTORE_PWD;
        trustStore.load(tsIn, truststorePwd.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(trustStore);

        //创建SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return sslContext;
    }
}
