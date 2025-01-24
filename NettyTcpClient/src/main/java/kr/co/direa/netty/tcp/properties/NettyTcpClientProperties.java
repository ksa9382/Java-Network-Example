package kr.co.direa.netty.tcp.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties(prefix = "netty.tcp.client")
@Getter
@RequiredArgsConstructor
public class NettyTcpClientProperties {
	private final String peerIp;
	private final int peerPort;
}
