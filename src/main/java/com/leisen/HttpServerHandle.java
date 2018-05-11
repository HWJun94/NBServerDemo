package com.leisen;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.http.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class HttpServerHandle extends IoHandlerAdapter {
    private LSHttpRequestImpl request;
    private Logger logger = LoggerFactory.getLogger(HttpServerHandle.class);

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    /**
     * 处理客户端请求的方法，这里实现业务逻辑
     * @param session 该请求对应的session
     * @param message 经过Decoder解码的消息
     * @throws Exception
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message instanceof HttpRequest) {
            request = (LSHttpRequestImpl) message;
            logger.info("Received: Header:\n{}", request.toString());
            logger.info("Received: Body:\n{}", request.getBody());

        }else if (message instanceof IoBuffer) { //message包含body部分，已在解码器中处理，可不必理会
//            System.out.println("--------IoBuffer--------");
        }else if (message instanceof HttpEndOfContent) { //请求接收结束，请求内容全部收到
//            System.out.println("------HttpEndOfContent------");
            sendResponse(session, request);
        }
    }

    /**
     * 向请求客户端发送应答响应
     * @param session 该请求对应的session
     * @param message 客户端请求request
     */
    public void sendResponse(IoSession session, HttpRequest message) {
        //响应
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Server", "Mina 2.0.16/Mac OS X 10.11.6");
        headers.put("Content-Type", "text/html; charset=utf-8");

        String responseStr = HttpStatus.SUCCESS_OK.line();
        IoBuffer responseBuff = IoBuffer.wrap(responseStr.getBytes());
        headers.put("Content-Length", String.valueOf(responseBuff.remaining()));
        LSHttpResponseImpl response = new LSHttpResponseImpl(HttpVersion.HTTP_1_1, HttpStatus.SUCCESS_OK, headers, responseStr);
        session.write(response);
//        session.write(responseBuff);
        session.write(new HttpEndOfContent());
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        if (message instanceof HttpEndOfContent)
            session.closeNow();
    }
}
