package kr.co.direa.nettyserver.run;

import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncServerRun {
	private final ServerBootstrap serverBootstrap;
	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workerGroup;
	private final int serverPort;

	@PostConstruct
	public void run() throws Exception {
		try {
			// Netty 서버 시작
			ChannelFuture channelFuture = serverBootstrap.bind(serverPort).sync();
			log.debug("Netty Server started on port: " + serverPort);

			// 서버 채널이 닫힐 때까지 대기
			channelFuture.channel().closeFuture().sync();
		} finally {
			// 자원 정리
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
