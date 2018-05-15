package com.leisen.http;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.http.api.HttpEndOfContent;
import org.apache.mina.http.api.HttpResponse;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Map;

public class LSHttpServerEncoder implements ProtocolEncoder {
    private static final CharsetEncoder ENCODER = Charset.forName("UTF-8").newEncoder();


    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out) throws Exception {
        if (message instanceof HttpResponse) {
            LSHttpResponseImpl msg = (LSHttpResponseImpl)message;
            StringBuilder sb = new StringBuilder(msg.getStatus().line());
            Iterator var6 = msg.getHeaders().entrySet().iterator();

            while(var6.hasNext()) {
                Map.Entry<String, String> header = (Map.Entry<String, String>)var6.next();
                sb.append((String)header.getKey());
                sb.append(": ");
                sb.append((String)header.getValue());
                sb.append("\r\n");
            }

            sb.append("\r\n");
            String body = msg.getBody();
            sb.append(body);
            sb.append("\r\n");
            IoBuffer buf = IoBuffer.allocate(sb.length()).setAutoExpand(true);
            buf.putString(sb.toString(), ENCODER);
            buf.flip();
            out.write(buf);
        } else if (message instanceof ByteBuffer) {
            out.write(message);
        } else if (message instanceof HttpEndOfContent) {
        }
    }

    public void dispose(IoSession ioSession) throws Exception {

    }
}
