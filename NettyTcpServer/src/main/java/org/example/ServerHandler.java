package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LogManager.getLogger(ServerHandler.class);

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
		ByteBuf buf = (ByteBuf)msg;
		byte[] rawData = new byte[buf.readableBytes()];
		buf.readBytes(rawData);

		String data = new String(rawData);
		log.debug("Received Message: [{}]", data);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error(cause.getStackTrace());
		super.exceptionCaught(ctx, cause);
	}
}