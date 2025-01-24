package kr.co.direa.nettyserver.decoder;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import kr.co.direa.nettyserver.handler.DefaultNettyServerHandler;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
	private final DefaultNettyServerHandler nettyServerHandler;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		// Inbound Handlers: 들어오는 데이터를 처리
		pipeline.addLast(new StringDecoder());

		// Outbound Handlers: 나가는 데이터를 처리
		pipeline.addLast(new StringEncoder());

		// Custom Handler
		pipeline.addLast(nettyServerHandler);
	}
}