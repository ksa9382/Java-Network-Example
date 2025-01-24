package kr.co.direa.netty.tcp.component;

import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.PostConstruct;
import kr.co.direa.netty.tcp.handler.TcpSyncClientHandler;
import kr.co.direa.netty.tcp.properties.NettyTcpClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NettyTcpClient {
	private final NettyTcpClientProperties properties;

	@PostConstruct
	public void init() throws Exception {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup())
			.remoteAddress(properties.getPeerIp(), properties.getPeerPort())
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					socketChannel.pipeline().addLast(new TcpSyncClientHandler());
				}
			})
			.option(ChannelOption.SO_KEEPALIVE, true);

		Channel channel = bootstrap.connect().sync().channel();

		// Thread.sleep(5000);
		//
		// channel.close().sync();
	}
}
