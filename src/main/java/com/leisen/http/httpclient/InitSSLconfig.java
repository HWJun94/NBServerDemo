package com.leisen.http.httpclient;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import com.leisen.http.LSSSLContextFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/*import com.sun.net.ssl.SSLContext;
import com.sun.tools.classfile.StackMapTable_attribute.verification_type_info;
*/
public class InitSSLconfig {
    public static CloseableHttpClient httpClient;
    public void initSSLConfigForTwoWay()throws Exception{
//        //Password of certificates.
//        String selfcertpwd ="IoM@1234";
//        String trustcapwd="Huawei@123";
//        //1 Import your own certificate
//        String demo_base_Path= System.getProperty("user.dir");
//        String selfcerpath = demo_base_Path+"/src/zhengshu/outgoing.CertwithKey.pkcs12";
//        String trustcapath = demo_base_Path+"/src/zhengshu/ca.jks";
//        KeyStore selfCert= KeyStore.getInstance("pkcs12");
//        selfCert.load(new FileInputStream(selfcerpath), selfcertpwd.toCharArray());
//        KeyManagerFactory kmf= KeyManagerFactory.getInstance("sunx509");
//        kmf.init(selfCert, selfcertpwd.toCharArray());
//
//        //2 Import the CA certificate of the server,
//        KeyStore caCert= KeyStore.getInstance("jks");
//        caCert.load(new FileInputStream(trustcapath),trustcapwd.toCharArray());
//        TrustManagerFactory tmf=TrustManagerFactory.getInstance("sunx509");
//        tmf.init(caCert);
//
//        javax.net.ssl.SSLContext sc=javax.net.ssl.SSLContext.getInstance("TLS");
//        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        //3 Set the domain name to not verify
        SSLConnectionSocketFactory sslsf= new SSLConnectionSocketFactory(LSSSLContextFactory.getInstance(false),new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });

        httpClient=HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

}