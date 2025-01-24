package kr.co.direa.basicsocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.direa.basicsocket.client.PlainTextEchoClient;
import kr.co.direa.basicsocket.properties.TcpClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TcpClientConfig {
	private final TcpClientProperties tcpClientProperties;

	@Bean
	public PlainTextEchoClient plainTextEchoClient() {
		log.info(tcpClientProperties.toString());

		PlainTextEchoClient plainTextEchoClient = new PlainTextEchoClient();
		plainTextEchoClient.setRemoteIp(tcpClientProperties.getRemoteIp());
		plainTextEchoClient.setRemotePort(tcpClientProperties.getRemotePort());
		return plainTextEchoClient;
	}
}
