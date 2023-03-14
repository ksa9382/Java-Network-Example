package Bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.channels.ServerSocketChannel;

public class ServerBootstrapExample {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap bs = new ServerBootstrap();

        bs.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(33006)
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                        System.out.println("Received Data.");
                    }
                });

        ChannelFuture future = bs.bind();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess() ) System.out.println("Server bound");
                else {
                    System.err.println("Bound attempt failed.");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
