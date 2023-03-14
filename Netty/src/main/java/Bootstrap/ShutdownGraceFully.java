package Bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

public class ShutdownGraceFully {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

                    }
                });

        Future<?> future = group.shutdownGracefully();  // 리소스를 해제하고 현재 이용 중인 모든 Channel을 닫음
                                                        // 대기 중인 이벤트와 작업을 모두 처리한 다음 모든 활성 스레드를 해제
        future.syncUninterruptibly();   // group이 종료될 때까지 진행을 블로킹
    }
}
