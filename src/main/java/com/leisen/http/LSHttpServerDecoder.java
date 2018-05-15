package com.leisen.http;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.http.*;
import org.apache.mina.http.api.HttpEndOfContent;
import org.apache.mina.http.api.HttpMethod;
import org.apache.mina.http.api.HttpStatus;
import org.apache.mina.http.api.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LSHttpServerDecoder implements ProtocolDecoder {
    private static final Logger LOG = LoggerFactory.getLogger(LSHttpServerDecoder.class);
    private static final String DECODER_STATE_ATT = "http.ds";
    private static final String PARTIAL_HEAD_ATT = "http.ph";
    private static final String BODY_REMAINING_BYTES = "http.brb";
    public static final Pattern REQUEST_LINE_PATTERN = Pattern.compile(" ");
    public static final Pattern QUERY_STRING_PATTERN = Pattern.compile("\\?");
    public static final Pattern PARAM_STRING_PATTERN = Pattern.compile("\\&|;");
    public static final Pattern KEY_VALUE_PATTERN = Pattern.compile("=");
    public static final Pattern RAW_VALUE_PATTERN = Pattern.compile("\\r\\n\\r\\n");
    public static final Pattern HEADERS_BODY_PATTERN = Pattern.compile("\\r\\n");
    public static final Pattern HEADER_VALUE_PATTERN = Pattern.compile(":");
    public static final Pattern COOKIE_SEPARATOR_PATTERN = Pattern.compile(";");

    public LSHttpServerDecoder() {
    }

    /**
     * Http消息解码方法
     * @param session 贯穿整个filer链的IoSession对象
     * @param msg 接收到的字节流
     * @param out 负责传递解码消息的ProtocolDecoderOutput
     */
    public void decode(IoSession session, IoBuffer msg, ProtocolDecoderOutput out) {
        DecoderState state = (DecoderState)session.getAttribute("http.ds");
        if (null == state) {
            session.setAttribute("http.ds", DecoderState.NEW);
            state = (DecoderState)session.getAttribute("http.ds");
        }

        switch(state) {
            case HEAD:
                LOG.debug("decoding HEAD");
                ByteBuffer oldBuffer = (ByteBuffer)session.getAttribute("http.ph");
                msg = IoBuffer.allocate(oldBuffer.remaining() + msg.remaining()).put(oldBuffer).put(msg).flip();
            case NEW:
                LOG.debug("decoding NEW");
                HttpRequestImpl rq = this.parseHttpRequestHead(msg.buf());
                if (rq == null) {
                    ByteBuffer partial = ByteBuffer.allocate(msg.remaining());
                    partial.put(msg.buf());
                    partial.flip();
                    session.setAttribute("http.ph", partial);
                    session.setAttribute("http.ds", DecoderState.HEAD);
                    break;
                } else {
                    out.write(rq);
                    String contentLen = rq.getHeader("content-length");
                    if (contentLen == null) {
                        LOG.debug("request without content");
                        session.setAttribute("http.ds", DecoderState.NEW);
                        out.write(new HttpEndOfContent());
                        break;
                    } else {
                        LOG.debug("found content len : {}", contentLen);
                        session.setAttribute("http.brb", Integer.valueOf(contentLen));
                        session.setAttribute("http.ds", DecoderState.BODY);
                    }
                }
            case BODY:
                LOG.debug("decoding BODY: {} bytes", msg.remaining());
                int chunkSize = msg.remaining();
                if (chunkSize != 0) {
                    IoBuffer wb = IoBuffer.allocate(msg.remaining());
                    wb.put(msg);
                    wb.flip();
//                    String raw = new String(wb.buf().array(), 0, wb.buf().limit());
//                    System.out.println(raw);
                    out.write(wb);
                }

                msg.position(msg.limit());
                int remaining = ((Integer)session.getAttribute("http.brb")).intValue();
                remaining -= chunkSize;
                if (remaining <= 0) {
                    LOG.debug("end of HTTP body");
                    session.setAttribute("http.ds", DecoderState.NEW);
                    session.removeAttribute("http.brb");
                    out.write(new HttpEndOfContent());
                } else {
                    session.setAttribute("http.brb", remaining);
                }
                break;
            default:
                throw new HttpException(HttpStatus.CLIENT_ERROR_BAD_REQUEST, "Unknonwn decoder state : " + state);
        }

    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
    }

    public void dispose(IoSession session) throws Exception {
    }

    /**
     * 解析http请求的方法，字节转码字符
     * @param buffer 接收到的http字节流
     * @return 解析完成后返回一个HttpRequestImpl的对象
     */
    private HttpRequestImpl parseHttpRequestHead(ByteBuffer buffer) {
//        System.out.println(buffer.capacity());
        String raw = new String(buffer.array(), 0, buffer.limit());
        String[] headersAndBody = RAW_VALUE_PATTERN.split(raw, -1);
        if (headersAndBody.length <= 1) {
            return null;
        } else {
            String[] headerFields = HEADERS_BODY_PATTERN.split(headersAndBody[0]);
            headerFields = ArrayUtil.dropFromEndWhile(headerFields, "");
            String requestLine = headerFields[0];
            Map<String, String> generalHeaders = new HashMap<String, String>();

            for(int i = 1; i < headerFields.length; ++i) {
                String[] header = HEADER_VALUE_PATTERN.split(headerFields[i]);
                generalHeaders.put(header[0].toLowerCase(), header[1].trim());
            }

            String[] elements = REQUEST_LINE_PATTERN.split(requestLine);
            HttpMethod method = HttpMethod.valueOf(elements[0]);
            HttpVersion version = HttpVersion.fromString(elements[2]);
            String[] pathFrags = QUERY_STRING_PATTERN.split(elements[1]);
            String requestedPath = pathFrags[0];
            String body = (headersAndBody.length == 2 && buffer.limit() <2048) ? headersAndBody[1] : "";
            String queryString = (pathFrags.length == 2 && method == HttpMethod.GET) ? pathFrags[1] : "";
            queryString = generalHeaders.containsValue("application/x-www-form-urlencoded") ? body : queryString;
            buffer.position(headersAndBody[0].length() + 4);
            return new LSHttpRequestImpl(version, method, requestedPath, queryString, generalHeaders, body);
        }
    }
}
