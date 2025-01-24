package kr.co.direa.nettyserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import kr.co.direa.nettyserver.decoder.NettyServerInitializer;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class NettyServerConfig {

	private final NettyServerInitializer nettyServerInitializer;

	@Value("${server.port}")
	private int serverPort;
	@Value("${server.netty.boss-thread-count}")
	private int bossThreadCount;
	@Value("${server.netty.worker-thread-count}")
	private int workerThreadCount;
	@Value("${server.netty.backlog}")
	private int backlog;
	@Value("${server.netty.keep-alive}")
	private boolean keepAlive;
	@Value("${server.netty.send-buffer-size}")
	private int sendBufferSize;
	@Value("${server.netty.receive-buffer-size}")
	private int receiveBufferSize;
	@Value("${server.netty.write-buffer-water-mark.low}")
	private int writeBufferLowWaterMark;
	@Value("${server.netty.write-buffer-water-mark.high}")
	private int writeBufferHighWaterMark;

	@Bean
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(bossThreadCount);
	}

	@Bean
	public NioEventLoopGroup workerGroup() {
		return new NioEventLoopGroup(workerThreadCount);
	}

	@Bean
	public ServerBootstrap serverBootstrap(NioEventLoopGroup bossGroup, NioEventLoopGroup workerGroup) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(nettyServerInitializer)
			.option(ChannelOption.SO_BACKLOG, backlog) // 대기 연결 요청 큐 크기
			.option(ChannelOption.SO_REUSEADDR, true) // 소켓 재사용
			.childOption(ChannelOption.SO_KEEPALIVE, keepAlive) // KeepAlive 활성화
			.childOption(ChannelOption.SO_SNDBUF, sendBufferSize) // 송신 버퍼 크기
			.childOption(ChannelOption.SO_RCVBUF, receiveBufferSize) // 수신 버퍼 크기
			.childOption(ChannelOption.TCP_NODELAY, true) // Nagle 알고리즘 비활성화
			.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(
				writeBufferLowWaterMark, writeBufferHighWaterMark)) // 쓰기 버퍼 워터마크
			.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT); // PooledByteBuf 사용

		return bootstrap;
	}

	@Bean
	public int serverPort() {
		return serverPort;
	}
}