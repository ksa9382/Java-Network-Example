package org.gtkim.example.nettyWrap.tcp.adapter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public class AsyncTcpClient {
	private final String remoteIp;
	private final int remotePort;

	private final ChannelInboundHandler handler;

	private Bootstrap bootstrap;
	private Channel channel;

	public AsyncTcpClient init() throws Exception {
		bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup())
			.remoteAddress(remoteIp, remotePort)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					socketChannel.pipeline().addLast(handler);
				}
			})
			.option(ChannelOption.SO_KEEPALIVE, true);

		return this;
	}

	public void connect() {
		try {
			ChannelFuture future = bootstrap.connect().syncUninterruptibly();
			channel = future.channel();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public boolean isConnected() {
		return channel != null && channel.isOpen();
	}

	public void disconnect() {
		try {
			channel.disconnect().sync();
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
	}

	public void send(byte[] message) {
		try {
			if (channel.isOpen()) {
				ByteBuf buf = Unpooled.copiedBuffer(message);
				//                channel.writeAndFlush(buf).addListener(new ChannelFutureListener() {
				//                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
				//                        log.debug("Success to send data to remote host. [" + channelFuture.channel().remoteAddress() + "]");
				//                    }
				//                }).sync();

				channel.writeAndFlush(buf).sync();

				// channel.writeAndFlush(buf).addListener(future -> {
				// 		ChannelFuture channelFuture = (ChannelFuture)future;
				// 		log.debug("Success to send data to remote host. [" + channelFuture.channel().remoteAddress() + "]");
				// 	}
				// ).sync();
			} else {
				log.error("Failed to send message: [" + new String(message) + "]. A connection is closed.");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
