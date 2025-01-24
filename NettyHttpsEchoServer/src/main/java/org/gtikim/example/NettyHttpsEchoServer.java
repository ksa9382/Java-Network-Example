package org.gtikim.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Properties;

import javax.net.ssl.KeyManagerFactory;

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
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.log4j.Log4j2;

/**
 * Hello world!
 *
 */
@Log4j2
public class NettyHttpsEchoServer {
	public static void main(String[] args) throws Exception {
		String workspace = System.getProperty("user.dir");
		log.debug("Current Directory: [" + workspace + "]");

		Properties prop = loadProperties(
			workspace + File.separator
				+ "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + "application.properties");

		int port = Integer.parseInt(prop.getProperty("serverPort"));

		KeyStore keyStore = KeyStore.getInstance("JKS");
		FileInputStream keyStoreFileStream = new FileInputStream("C:\\Users\\DIR-N-171\\.cert\\server.jks");
		keyStore.load(keyStoreFileStream, "ks24453944".toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(keyStore, "ks24453944".toCharArray());

		// SSL/TLS 설정
		//        SelfSignedCertificate ssc = new SelfSignedCertificate();
		final SslContext sslContext = SslContextBuilder
			.forServer(kmf)
			.protocols("TLSv1.2")
			.build();

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						ChannelPipeline pipeline = ch.pipeline();

						pipeline.addLast(sslContext.newHandler(ch.alloc()));
						pipeline.addLast(new HttpServerCodec());
						pipeline.addLast(new HttpObjectAggregator(65536));
						pipeline.addLast(new HttpsEchoServerHandler());
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
