package Bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class ClientChannelInsideServerChannel {
    public static void main(String[] args) {
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        new SimpleChannelInboundHandler<ByteBuf>() {
                            ChannelFuture connectFuture;

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                // 원격 호스트에 연결할 Bootstrap을 구성
                                Bootstrap b = new Bootstrap();
                                b.channel(NioSocketChannel.class)
                                        .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                                            @Override
                                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                                                System.out.println("Received Data.");
                                            }
                                        });
                                // ServerSocketChannel로부터 생성된 Accepted Channel의 EventLoop를 공유
                                b.group(ctx.channel().eventLoop());
                                connectFuture = b.connect(new InetSocketAddress("localhost", 33007));
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                                if (connectFuture.isDone()) {
                                    // 연결이 완료되면 필요한 데이터 작업
                                }
                            }
                        }
                );

        ChannelFuture future = sb.bind(new InetSocketAddress(33006));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) System.out.println("Server bound");
                else {
                    System.err.println("Bind attempt failed.");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}