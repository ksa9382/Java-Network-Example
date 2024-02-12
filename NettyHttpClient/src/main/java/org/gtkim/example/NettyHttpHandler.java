package org.gtkim.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyHttpHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static final Logger log = LogManager.getLogger(NettyHttpHandler.class);
    private EventLoopGroup group;
    private SocketChannel sc;
    private int count=0;

    public NettyHttpHandler(EventLoopGroup group, SocketChannel sc) {
        this.group = group;
        this.sc =sc;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;

            log.debug("STATUS: " + response.status());
            log.debug("VERSION: " + response.protocolVersion());

            if (!response.headers().isEmpty()) {
                for (CharSequence name : response.headers().names()) {
                    for (CharSequence value : response.headers().getAll(name)) {
                        log.debug("HEADER: " + name + " = " + value);
                    }
                }
            }

            if (HttpUtil.isTransferEncodingChunked(response)) {
                log.debug("CHUNKED CONTENT {");
            } else {
                log.debug("CONTENT {");
            }
        }
        if (msg instanceof HttpContent) {
            count++;
            HttpContent content = (HttpContent) msg;
            log.debug(count+". create url = "+content.content().toString(CharsetUtil.UTF_8));

            if (content instanceof LastHttpContent) {
                log.debug("} END OF CONTENT");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        ctx.close();
        sc.close();
        group.shutdownGracefully();
    }
}
