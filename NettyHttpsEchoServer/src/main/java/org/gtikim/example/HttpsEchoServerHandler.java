package org.gtikim.example;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HttpsEchoServerHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof FullHttpRequest) {
			FullHttpRequest httpRequest = (FullHttpRequest)msg;
			ByteBuf content = httpRequest.content();
			String requestContent = content.toString(StandardCharsets.UTF_8);

			log.debug("Received HTTP Request:\n" + requestContent);

			FullHttpResponse httpResponse = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK,
				Unpooled.copiedBuffer(requestContent, StandardCharsets.UTF_8)
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
