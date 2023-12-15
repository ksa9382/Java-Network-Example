package org.gtkim.nettyWrap.tcp.adapter.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gtkim.ProxyServer;
import org.gtkim.nettyWrap.tcp.adapter.AsyncTcpClient;

@ChannelHandler.Sharable
public class ProxyServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LogManager.getLogger(ProxyServerHandler.class);

    private ProxyServer parent;

    public void bindParent(ProxyServer parent) {
        this.parent = parent;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.debug("remote host[" + ctx.channel().remoteAddress() + "] is connected.");

        ctx.pipeline().addLast(new ExceptionHandler());

        parent.startProxyClient();
        parent.startClient();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.debug("remote host[" + ctx.channel().remoteAddress() + "] is disconnected.");

        AsyncTcpClient proxyClient = parent.getProxyClient();

        log.debug("try proxy disconnecting to [" + proxyClient.getRemoteIp() + ":" + proxyClient.getRemotePort() + "]");
        proxyClient.disconnect();
        log.debug("proxy host[" + proxyClient.getRemoteIp() + ":" + proxyClient.getRemotePort() + " is successfully disconnected.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = Unpooled.copiedBuffer((ByteBuf)msg);
        byte[] rawData = new byte[buf.readableBytes()];
        buf.readBytes(rawData);
        String data = new String(rawData);

        AsyncTcpClient proxyClient = parent.getProxyClient();

        log.debug("Received: [" + rawData.length + " bytes], [" + data + "]");
        proxyClient.send(rawData);
        log.debug("Success to send data to proxy.");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("Unhandled Exception. [" + cause.getCause() + "]");
    }
}
