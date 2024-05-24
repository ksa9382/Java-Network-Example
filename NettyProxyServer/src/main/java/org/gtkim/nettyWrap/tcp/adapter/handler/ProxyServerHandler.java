package org.gtkim.nettyWrap.tcp.adapter.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.gtkim.ProxyServer;
import org.gtkim.example.nettyWrap.tcp.adapter.AsyncTcpClient;
import org.gtkim.nettyWrap.tcp.adapter.response.ResponseCreator;

import java.nio.charset.StandardCharsets;

@Slf4j
@Setter
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class ProxyServerHandler extends ChannelInboundHandlerAdapter {
    private ProxyServer parent;
    private final ResponseCreator responseCreator;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("remote host[" + ctx.channel().remoteAddress() + "] is connected.");

        ctx.pipeline().addLast(new ExceptionHandler());

        parent.startProxyClient();
//        parent.startClient();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("remote host[" + ctx.channel().remoteAddress() + "] is disconnected.");

        AsyncTcpClient proxyClient = parent.getProxyClient();

        log.info("try proxy disconnecting to [" + proxyClient.getRemoteIp() + ":" + proxyClient.getRemotePort() + "]");
        proxyClient.disconnect();
        log.info("proxy host[" + proxyClient.getRemoteIp() + ":" + proxyClient.getRemotePort() + " is successfully disconnected.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = Unpooled.copiedBuffer((ByteBuf)msg);

        byte[] requestRaw = new byte[buf.readableBytes()];
        buf.readBytes(requestRaw);

        String requestStr = new String(requestRaw);
        log.info("Request: [" + requestStr + "], Length: [" + requestRaw.length + "bytes]");

        AsyncTcpClient proxyClient = parent.getProxyClient();
        String response = responseCreator.makeResponse(requestStr);

        proxyClient.send(response.getBytes(StandardCharsets.UTF_8));
        log.info("Success to send data to proxy.");
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
