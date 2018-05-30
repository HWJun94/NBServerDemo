package com.leisen;

import com.leisen.db.DeviceStorage;
import com.leisen.http.HttpServerHandle;
import com.leisen.http.LSHttpServerDecoder;
import com.leisen.http.LSHttpServerEncoder;
import com.leisen.http.LSSSLContextFactory;
import com.leisen.mqtt.MqttPublishServer;
import com.leisen.msgqueue.MessageStorage;
import com.leisen.msgqueue.QueueMessageConsumer;
import com.leisen.util.ConfigUtil;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;

public class NBServerDemo {

    public static void main(String[] args) throws Exception {
        //load the configuration
        ConfigUtil.configure("config.properties");

        // init the storage
        MessageStorage messageStorage = new MessageStorage();
        DeviceStorage deviceStorage = new DeviceStorage();
        deviceStorage.loadFromDB();

        // init the mqttclient
        MqttPublishServer mqttPublishServer = new MqttPublishServer();
        mqttPublishServer.initMQTT();
        mqttPublishServer.connect();

        // submit comsumers to ThreadPool
        for (int i = 0; i < messageStorage.getNthreadsConsumer(); i++) {
            QueueMessageConsumer queueMessageConsumer = new QueueMessageConsumer(mqttPublishServer);
            MessageStorage.executeConsumer(queueMessageConsumer);
        }

        //init the mina Server
        IoAcceptor acceptor = new NioSocketAcceptor();
        SslFilter sslFilter = new SslFilter(LSSSLContextFactory.getInstance(true));
//        sslFilter.setNeedClientAuth(true);
        acceptor.getFilterChain().addLast("sslfilter", sslFilter);
//        acceptor.getSessionConfig().setReadBufferSize(1024*1024*2);
        LoggingFilter loggingFilter = new LoggingFilter("loggingfilterbefore");
        acceptor.getFilterChain().addLast("loggingfilterbefore", loggingFilter);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new LSHttpServerEncoder(), new LSHttpServerDecoder()));
        acceptor.setHandler(new HttpServerHandle());
        acceptor.bind(new InetSocketAddress(8443));
    }
}
