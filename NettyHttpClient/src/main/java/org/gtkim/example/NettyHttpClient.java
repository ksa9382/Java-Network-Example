package org.gtkim.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

@Slf4j
public class NettyHttpClient {
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

    public void send(HttpRequest request, boolean isMultipart) {
        cf.channel().writeAndFlush(request);
        log.debug(request.toString());
    }

    public void close() {
        cf.channel().close();
        group.shutdownGracefully();
    }
}
