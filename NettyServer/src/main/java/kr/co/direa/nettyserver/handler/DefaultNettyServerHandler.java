package kr.co.direa.nettyserver.handler;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import kr.co.direa.nettyserver.processor.AsyncProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultNettyServerHandler extends SimpleChannelInboundHandler<String> {

	private final AsyncProcessor asyncProcessor;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("remote host[{}] is connected.", ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("remote host[{}] is disconnected.", ctx.channel().remoteAddress());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) {
		// 비동기 작업 위임
		asyncProcessor.process(msg, ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("Exception caught in NettyHandler", cause);
		ctx.close();
	}
}