package org.gtkim.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.util.AsciiString;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class HttpRequestBuilder {
    private FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "");

    String uri;

    public HttpRequestBuilder version(String httpVersion) {
        HttpVersion realHttpVersion = HttpVersion.HTTP_1_1;
        if ("1.0".equals(httpVersion))
            realHttpVersion = HttpVersion.HTTP_1_0;

        return version(realHttpVersion);
    }

    public HttpRequestBuilder version(HttpVersion httpVersion) {
        request.setProtocolVersion(httpVersion);
        return this;
    }

    public HttpRequestBuilder method(String method) {
        HttpMethod realHttpMethod = HttpMethod.POST;

        switch (method) {
            case "GET":
                realHttpMethod = HttpMethod.GET;
                break;
            case "PUT":
                realHttpMethod = HttpMethod.PUT;
                break;
            case "CONNECT":
                realHttpMethod = HttpMethod.CONNECT;
                break;
            case "DELETE":
                realHttpMethod = HttpMethod.DELETE;
                break;
            case "HEAD":
                realHttpMethod = HttpMethod.HEAD;
                break;
            case "PATCH":
                realHttpMethod = HttpMethod.PATCH;
                break;
            case "TRACE":
                realHttpMethod = HttpMethod.TRACE;
                break;
            case "OPTIONS":
                realHttpMethod = HttpMethod.OPTIONS;
                break;
        }

        return method(realHttpMethod);
    }

    public HttpRequestBuilder method(HttpMethod method) {
        request.setMethod(method);
        return this;
    }

    public HttpRequestBuilder uri(String uri) {
        this.uri = uri;
        request.setUri(uri);
        return this;
    }

    public HttpRequestBuilder contentType(String contentType) {
        AsciiString realContentType = HttpHeaderValues.APPLICATION_JSON;
        switch (contentType) {
            case "xml":
                realContentType = HttpHeaderValues.APPLICATION_XML;
                break;
            case "plain":
                realContentType = HttpHeaderValues.TEXT_PLAIN;
                break;
            case "www_form":
                realContentType = HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED;
        }

        return contentType(realContentType);
    }

    public HttpRequestBuilder contentType(AsciiString contentType) {
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
        return this;
    }

    public HttpRequestBuilder content(String content, String encoding) {
        Charset charset;
        switch (encoding) {
            case "EUC-KR":
                charset = Charset.forName("EUC-KR");
                break;
            case "UTF-8":
            default:
                charset = StandardCharsets.UTF_8;
        }

        ByteBuf byteBuf = Unpooled.copiedBuffer(content, charset);
        request.headers().set(HttpHeaderNames.CONTENT_ENCODING, charset);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
        request.content().clear().writeBytes(byteBuf);

        return this;
    }

    public HttpRequest build() {
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);


        return request;
    }
}
