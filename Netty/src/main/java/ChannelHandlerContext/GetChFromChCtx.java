package ChannelHandlerContext;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class GetChFromChCtx {
    public static void main(String[] args) throws InterruptedException{
        Channel ch = new NioSocketChannel();

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();

        b.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress("127.0.0.1", 12345)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<Object>() {
                            @Override
                            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("Registered");
                                Channel parentChannel = ctx.channel();
                                parentChannel.pipeline().fireChannelUnregistered();
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

                            }

                            @Override
                            public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("Unregistered");
                            }
                        });
                    }
                });



        ChannelFuture future = b.register().sync();
    }
}
