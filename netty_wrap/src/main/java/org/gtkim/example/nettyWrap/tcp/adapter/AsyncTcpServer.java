package org.gtkim.example.nettyWrap.tcp.adapter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class AsyncTcpServer implements Runnable {
    private final int port;
    private final ChannelInboundHandlerAdapter handler;

    private EventLoopGroup boss;
    private EventLoopGroup worker;

    private ServerBootstrap serverBootstrap;

    public AsyncTcpServer init() throws Exception{
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();

        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .localAddress(port)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
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
            log.error(e.getMessage());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
