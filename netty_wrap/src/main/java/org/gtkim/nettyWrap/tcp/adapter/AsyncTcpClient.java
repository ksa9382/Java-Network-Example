package org.gtkim.nettyWrap.tcp.adapter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class AsyncTcpClient {
    private String remoteIp;
    private int remotePort;

    private ChannelInboundHandler handler;

    private EventLoopGroup worker;

    private Bootstrap bootstrap;
    private Channel channel;
    private ChannelHandlerContext ctx;

    public AsyncTcpClient(String remoteIp, int remotePort, ChannelInboundHandler handler) {
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
        this.handler = handler;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public AsyncTcpClient init() throws Exception{
        worker = new NioEventLoopGroup();

        bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .remoteAddress(remoteIp, remotePort)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(handler);
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);

        return this;
    }

    public void connect() {
        try {
            ChannelFuture future = bootstrap.connect();
            channel = future.channel();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return channel.isOpen();
    }

    public void disconnect() {
        try {
            channel.disconnect().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int send(byte[] message) {
        int sendBytes = 0;
        try {
            if (channel.isOpen()) {
                ByteBuf buf = Unpooled.copiedBuffer(message);
                channel.writeAndFlush(buf).addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        System.out.println("Success to send data to remote host. [" + channelFuture.channel().remoteAddress() + "]");
                    }
                }).sync();
            } else {
                System.out.println("Failed to send message: [" + message + "]. A connection is closed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sendBytes;
    }
}
