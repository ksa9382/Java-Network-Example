package org.gtkim.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

/**
 * Hello world!
 *
 */
public class NettyHttpEchoServer {
	private static final Logger log = LogManager.getLogger(NettyHttpEchoServer.class);

	public static void main(String[] args) throws Exception {
		String workspace = System.getProperty("user.dir");
		log.debug("Current Directory: [" + workspace + "]");

		Properties prop = loadProperties(
			workspace + File.separator + ".." + File.separator + "resources" + File.separator
				+ "application.properties");
		final String mode = prop.getProperty("mode");
		final String sslPath = prop.getProperty("sslPath");
		final String sslPassword = prop.getProperty("sslPassword");

		int port = Integer.parseInt(prop.getProperty("serverPort"));

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						ChannelPipeline pipeline = ch.pipeline();

						if ("https".equalsIgnoreCase(mode)) {
							try {
								SslContext sslContext = SslContextFactory.createSslContext(sslPath, sslPassword);

								log.debug("Created ssl context..");

								pipeline.addLast(sslContext.newHandler(ch.alloc()));
							} catch (Exception e) {
								log.error(e.getCause());
							}
						}

						pipeline.addLast(new HttpServerCodec());
						pipeline.addLast(new HttpObjectAggregator(65536));
						pipeline.addLast(new HttpEchoServerHandler());
					}
				});

			log.debug("HTTP Echo Server started on port " + port);
			ChannelFuture future = serverBootstrap.bind(port).sync();

			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	private static Properties loadProperties(String path) {
		Properties prop = new Properties();

		try (BufferedReader bis = new BufferedReader(new FileReader(path))) {
			prop.load(bis);
			log.debug("properties file loaded. [" + prop + "]");
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return prop;
	}
}
