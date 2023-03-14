package Bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

public class UseAttribute {
    public static void main(String[] args) {
        final AttributeKey<Integer> id = AttributeKey.valueOf("ID");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        Integer idValue = ctx.channel().attr(id).get();
                        // ID 값을 이용하는 로직있다고 가정
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                        System.out.println("Received Data.");
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        bootstrap.attr(id, 123456); // id 특성을 저장. Bootstrap attr 메서드로 지정한 경우도 생성되는 channel에 특성이 적용
                                    // 즉, 클라이언트 Bootstrap을 통해 connect로 생성된 채널은 id값이 123456으로 설정
        ChannelFuture future = bootstrap.connect();
        future.syncUninterruptibly();
    }
}
