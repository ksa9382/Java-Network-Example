package org.gtkim;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable // Indicate that a class or method is safe to be used concurrently by multiple channels.
                         // This means that the state of the class or the method can be shared among multiple instances without the risk of race conditions or data corruption. By marking a class or a method with this annotation, you are indicating that it is thread-safe and can be reused across multiple channels, which can lead to improved performance and reduced memory usage.
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client [" + ctx.channel().remoteAddress() + "] is connected.");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client [" + ctx.channel().remoteAddress() + "] is disconnected.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;

        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));

        ctx.write(msg); // Not flush yet.
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // Flush waiting messages on the channel and close one.
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                        .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
