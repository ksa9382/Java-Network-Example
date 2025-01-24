package org.gtkim.nettyWrap.tcp.adapter.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExceptionHandler extends SimpleChannelInboundHandler<ByteBuf> {
	//    private static final Logger log = LogManager.getLogger(ExceptionHandler.class);
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
		// TODO
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		log.debug(String.valueOf(cause.getCause()));
	}
}
