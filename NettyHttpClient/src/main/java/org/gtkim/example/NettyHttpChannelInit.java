package org.gtkim.example;

import javax.net.ssl.SSLException;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class NettyHttpChannelInit extends ChannelInitializer<SocketChannel> {
	private final boolean ssl = false;
	private final EventLoopGroup group;

	public NettyHttpChannelInit(EventLoopGroup group) {
		this.group = group;
	}

	@Override
	protected void initChannel(SocketChannel sc) throws Exception {
		ChannelPipeline pipeline = sc.pipeline();
		if (ssl) {
			SslContext sslCtx = null;
			try {
				sslCtx = SslContextBuilder.forClient()
					.trustManager(InsecureTrustManagerFactory.INSTANCE).build();
				pipeline.addLast(sslCtx.newHandler(sc.alloc()));
			} catch (SSLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		//chunked 된 응답을 집계하는 코덱
		pipeline.addLast("chunked", new HttpObjectAggregator(1048576));
		pipeline.addLast("codec", new HttpClientCodec());
		pipeline.addLast(new NettyHttpHandler(group, sc));
	}
}
