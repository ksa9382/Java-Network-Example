package Bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class ClientBootstrapExample {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)                      // EventLoopGroup과
                .channel(NioSocketChannel.class)    // SocketChannel의 접두사는 항상 같아야 함
                .remoteAddress(new InetSocketAddress("localhost", 33006))
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
                        System.out.println("Received Data");
                    }
                });

        ChannelFuture future = bootstrap.connect();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) System.out.println("Connection established");
                else {
                    System.err.println("Connection attempt failed.");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
