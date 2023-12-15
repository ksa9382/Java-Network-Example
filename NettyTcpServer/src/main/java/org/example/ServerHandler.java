package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LogManager.getLogger(ServerHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] rawData = new byte[buf.readableBytes()];
        buf.readBytes(rawData);

        log.debug(new String(rawData));

        ByteBuf response = Unpooled.copiedBuffer(rawData);
        ctx.writeAndFlush(response);
    }
}
