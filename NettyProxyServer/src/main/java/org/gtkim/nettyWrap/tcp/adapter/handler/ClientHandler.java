package org.gtkim.nettyWrap.tcp.adapter.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gtkim.ProxyServer;

public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Logger log = LogManager.getLogger(ClientHandler.class);
    private ProxyServer parent;

    public ClientHandler(ProxyServer parent) {
        super();
        this.parent = parent;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug("Connected to remote host. [" + ctx.channel().remoteAddress() + "]");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("Disconnected from remote host. [" + ctx.channel().remoteAddress() + "]");

        while (!parent.reconnectClient()) {
            log.debug("retry connect.");
            Thread.sleep(3000);
        }
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
