package org.gtkim;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EchoServer {
	private final int port;
	private final ChannelHandler handler;

	public void start() throws Exception {
		// EventLoopGroup 생성
		EventLoopGroup boss = new NioEventLoopGroup(1);
		EventLoopGroup worker = new NioEventLoopGroup();

		// ServerBootStrap 생성(채널 지정, Address 지정, 핸들러 지정)
		ServerBootstrap b = new ServerBootstrap();

		try {
			b.group(boss, worker)
				.channel(NioServerSocketChannel.class)  // NIO 전송 채널을 이용하도록 지정
				.localAddress(port)                     // 지정된 포트를 이용해 소켓 주소를 설정
				.childOption(ChannelOption.TCP_NODELAY, true)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.SO_LINGER, 0)
				.childOption(ChannelOption.SO_REUSEADDR, true)
				.childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(40960))
				.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
				.childHandler(
					new ChannelInitializer<SocketChannel>() {     // EchoServerHandler 인스턴스를 채널의 Channel Pipeline으로 추가
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast(handler);
						}
					});
			// 서버를 동기식으로 바인딩
			ChannelFuture f = b.bind().sync();  // sync는 바인딩이 완료되기를 대기

			// 채널의 CloseFuture를 얻고 완료될 때까지 현재 스레드를 블로킹
			f.channel().closeFuture().sync();
		} finally {
			// EventLoopGroup을 종료하고 모든 리소스를 해제
			boss.shutdownGracefully().sync();
			worker.shutdownGracefully().sync();
		}
	}
}
