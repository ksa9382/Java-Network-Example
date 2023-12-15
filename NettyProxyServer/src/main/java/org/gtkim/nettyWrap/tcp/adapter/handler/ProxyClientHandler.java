package org.gtkim.nettyWrap.tcp.adapter.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gtkim.ProxyServer;
import org.gtkim.nettyWrap.tcp.adapter.AsyncTcpClient;

@ChannelHandler.Sharable
public class ProxyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Logger log = LogManager.getLogger(ProxyClientHandler.class);
    private ProxyServer parent;

    public ProxyClientHandler(ProxyServer parent) {
        this.parent = parent;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug("Connected to proxy[" + ctx.channel().remoteAddress() + "]");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("Disconnected from proxy[" + ctx.channel().remoteAddress() + "]");
    }

    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        ByteBuf copied = Unpooled.copiedBuffer(byteBuf);
        byte[] rawData = new byte[copied.readableBytes()];
        copied.readBytes(rawData);

        String data = new String(rawData);

        log.debug("Received response: [" + rawData.length + " bytes], Data: [" + data + "]");

        AsyncTcpClient client = parent.getClient();

        if (!client.isConnected()) {
            log.error("Client is not opened.");
            return;
        }

        client.send(rawData);

        log.debug("Success to send to remote host. [" + client.getRemoteIp() + ":" + client.getRemotePort() + "]");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("Unhandled Exception. [" + cause.getCause() + "]");
    }
}
