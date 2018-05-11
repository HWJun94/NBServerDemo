package com.leisen;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;

public class NBServerDemo {

    public static void main(String[] args) throws Exception {
        IoAcceptor acceptor = new NioSocketAcceptor();
        //创建ssl过滤器
        SslFilter sslFilter = new SslFilter(LSSSLContextFactory.getInstance());
        acceptor.getFilterChain().addLast("sslfilter", sslFilter);
//        acceptor.getSessionConfig().setReadBufferSize(1024*1024*2);
        acceptor.getFilterChain().addLast("loggingfilterbefore", new LoggingFilter("loggingfilterbefore"));
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new LSHttpServerEncoder(), new LSHttpServerDecoder()));
//        acceptor.getFilterChain().addLast("loggingfilterafter", new LoggingFilter("loggingfilterafter"));
        acceptor.setHandler(new HttpServerHandle());
        acceptor.bind(new InetSocketAddress(8443));
    }
}
