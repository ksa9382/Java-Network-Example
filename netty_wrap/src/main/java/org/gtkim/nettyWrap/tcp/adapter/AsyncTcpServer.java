package org.gtkim.nettyWrap.tcp.adapter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@ChannelHandler.Sharable
public class AsyncTcpServer implements Runnable {
    private int port;
    private ChannelInboundHandlerAdapter handler;

    private EventLoopGroup boss;
    private EventLoopGroup worker;

    private ServerBootstrap serverBootstrap;

    public AsyncTcpServer(int port, ChannelInboundHandlerAdapter handler) {
        this.port = port;
        this.handler = handler;
    }

    public int getPort() {
        return port;
    }

    public AsyncTcpServer init() throws Exception{
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .localAddress(port)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(handler);
                    }
                });

        return this;
    }

    public void run() {
        try {
            ChannelFuture future = serverBootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
