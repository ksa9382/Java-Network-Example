package org.gtkim.example;

import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.util.AsciiString;

public class HttpRequestBuilder {
    private HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "");
    boolean useMultipart = false;

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

    public HttpRequestBuilder multipart(boolean useMultipart) {
        this.useMultipart = useMultipart;
        return this;
    }

    public HttpRequest build() {
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        try {
            HttpRequestEncoder requestEncoder = new HttpRequestEncoder();
        }
        HttpPostRequestEncoder postRequestEncoder = new HttpPostRequestEncoder(request, useMultipart);

        if (!"".equals(url))
            postRequestEncoder.addBodyAttribute("url", url);

        request=postRequestEncoder.finalizeRequest();
        postRequestEncoder.close();

        return request;
    }
}
