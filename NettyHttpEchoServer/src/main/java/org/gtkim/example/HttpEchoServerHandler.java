package org.gtkim.example;

import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class HttpEchoServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LogManager.getLogger(HttpEchoServerHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.debug("Connected from [" + ctx.channel().remoteAddress() + "]..");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.debug("Connection is closed..[" + ctx.channel().remoteAddress() + "]..");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof FullHttpRequest) {
			FullHttpRequest httpRequest = (FullHttpRequest)msg;
			ByteBuf content = httpRequest.content();
			String requestContent = content.toString(Charset.forName("EUC-KR"));
			String wtcResponse = "00000018ACK0000 1_fepSvr13";

			log.debug("Received HTTP Request:\n" + requestContent);

			FullHttpResponse httpResponse = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK,
				// Unpooled.copiedBuffer(requestContent, Charset.forName("EUC-KR"))
				Unpooled.copiedBuffer(wtcResponse, Charset.forName("EUC-KR"))
			);

			ctx.writeAndFlush(httpResponse);
			log.debug("Echo received data.");

			ctx.close();
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error(cause.toString());
		ctx.close();
	}
}
