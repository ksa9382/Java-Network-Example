package discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler2
        extends SimpleChannelInboundHandler<Object> {

    // 리소스를 명시적으로 해제할 필요가 없음
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;

        while (in.isReadable()) {
            System.out.println((char) in.readByte());
            System.out.flush();
        }

        // 호출 끝나는 시점에 msg 참조가 무효화되므로 msg 참조를 어딘가로 저장하지 말아야 한다.
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
