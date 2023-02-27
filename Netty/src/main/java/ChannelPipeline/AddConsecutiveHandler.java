package ChannelPipeline;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.EventListener;

public class AddConsecutiveHandler {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(12777)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socCh) throws Exception {
                        for (int i = 0; i < 10; i++) {
                            if (i % 2 == 0) socCh.pipeline().addLast(new CustomOutboundHandler("Out ." + i));
                            else socCh.pipeline().addLast(new CustomInboundHandler("In ." + i));
                        }
                    }
                });

        try {
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            group.shutdownGracefully();
        }
    }
}
