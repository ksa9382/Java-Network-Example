package org.gtkim.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Getter
@ChannelHandler.Sharable
public class TgrmResponseHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LogManager.getLogger(TgrmResponseHandler.class);

    private final String mode;

    private final String tgrm;
    private final String encoding;

    private final byte[] response;

    public TgrmResponseHandler(String mode) {
        this(mode, "", "");
    }

    public TgrmResponseHandler(String mode, String tgrm, String encoding) {
        this.mode = mode;
        this.tgrm = tgrm;
        this.encoding = encoding;
        byte[] tempResponse;

        if (mode.equalsIgnoreCase("TGRM")) {
            try {
                tempResponse = this.tgrm.getBytes(encoding);
            } catch (UnsupportedEncodingException e) {
                tempResponse = this.tgrm.getBytes(StandardCharsets.UTF_8);
            }
            response = tempResponse;
        } else {
            response = null;
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] rawData = new byte[buf.readableBytes()];
        buf.readBytes(rawData);

        log.debug("Request Received.. [" + new String(rawData) + "]");

        ByteBuf responseBuf;
        if (mode.equalsIgnoreCase("TGRM")) {
            responseBuf = Unpooled.copiedBuffer(response);
        } else {
            responseBuf = Unpooled.copiedBuffer(rawData);
        }

        ctx.writeAndFlush(responseBuf);
    }
}
