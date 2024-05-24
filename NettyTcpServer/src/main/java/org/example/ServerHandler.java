package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LogManager.getLogger(ServerHandler.class);

    private static final Object blockKey = new Object();

    private static Map<String, String> txMap = new ConcurrentHashMap<>();

    private static final byte[] empty = new byte[0];

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client [" + ctx.channel().remoteAddress() + "] is connected.");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client [" + ctx.channel().remoteAddress() + "] is disconnected.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] rawData = new byte[buf.readableBytes()];
        buf.readBytes(rawData);

        String data = new String(rawData);

        synchronized (blockKey) {
            String key = data.substring(data.length() - 1 - 5);
            log.debug("key: [" + key + "]");

            String contained = txMap.get(key);
            if (!data.equals(contained)) {
                txMap.put(key, data);
                log.debug("Not Contained [" + data + "]");

                ByteBuf response = Unpooled.copiedBuffer(rawData);
                Thread.sleep(2000);

                ctx.writeAndFlush(response);

                txMap.remove(key);
            } else {
                log.debug("Contained [" + data + "]");

                ByteBuf emptyResponse = Unpooled.copiedBuffer(empty);
                ctx.writeAndFlush(emptyResponse);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getStackTrace());
        super.exceptionCaught(ctx, cause);
    }
}