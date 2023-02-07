package Echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buff = (ByteBuf)msg;

        System.out.println("Received: " + buff.toString(CharsetUtil.UTF_8));

//        ctx.write(msg); // Do not make the message written out to the wire. It is buffered internally and
//                        // then flushed out to the wire by ctx.flush().
//
//        ctx.flush();    // After call flush(), netty releases msg when it is written out to the wire.

        ctx.writeAndFlush(msg); // same result above that.
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();    // Print the result of stack trace.
        ctx.close();                // close the channel connected to remote host.
    }
}
