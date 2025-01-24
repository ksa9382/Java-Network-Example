package kr.co.direa.basicsocket.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "tcp")
public class TcpClientProperties {
	private String remoteIp;
	private int remotePort;
}
