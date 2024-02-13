package org.gtkim.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyHttpClient {
    static final Logger log = LogManager.getLogger(NettyHttpClient.class);

    ChannelFuture cf;
    EventLoopGroup group;

    public void connect(String host, int port) {

        group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new NettyHttpChannelInit(group));

            cf = b.connect(host, port).sync();
            log.debug("Connected.. Remote host: [" + cf.channel().remoteAddress() + "]");
//            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void createRequest(String host, int port, String url) throws Exception {
        HttpRequest request = null;
        HttpPostRequestEncoder postRequestEncoder = null;

//        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/create"
////                    ,Unpooled.copiedBuffer(url.getBytes(CharsetUtil.UTF_8))
//        );

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, url);
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
        request.headers().set(HttpHeaderNames.HOST, host+":"+port);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, url.length());

        postRequestEncoder = new HttpPostRequestEncoder(request, false);

        if (!"".equals(url))
            postRequestEncoder.addBodyAttribute("url", url);

        request=postRequestEncoder.finalizeRequest();
        postRequestEncoder.close();
//            cf.channel().writeAndFlush(request).addListener(ChannelFutureListener.CLOSE);
        cf.channel().writeAndFlush(request);
        log.debug(request.toString());
    }

    public void close() {
        cf.channel().close();
        group.shutdownGracefully();
    }
}
