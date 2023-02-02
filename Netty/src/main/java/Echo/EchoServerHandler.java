package Echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write(msg); // Does not make the message written out to the wire. It is buffered internally and
                        // then flushed out to the wire by ctx.flush().

        ctx.flush();    // After call flush(), netty releases msg when it is written out to the wire.

//        ctx.writeAndFlush(msg); // same result above that.
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
