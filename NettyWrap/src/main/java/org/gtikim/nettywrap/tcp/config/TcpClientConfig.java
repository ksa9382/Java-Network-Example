package org.gtikim.nettywrap.tcp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "netty.channel.client")
public class TcpClientConfig {
    private String remoteIp;

    private int remotePort;

    private int connectTimeout;

    private int readTimeout;

    private boolean keepAlive;

    private int keepAliveIdleTime;

    private int keepAliveInterval;

    private int keepAliveRetries;

    private int trafficClass;

    private int receiveBufferSize;

    private int sendBufferSize;

    private boolean tcpNoDelay;

    private int tcpNagleThreshold;

    private boolean soReuseAddr;

    private int soLinger;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TcpClientConfig{");
        sb.append("remoteIp='").append(remoteIp).append('\'');
        sb.append(", remotePort=").append(remotePort);
        sb.append(", connectTimeout=").append(connectTimeout);
        sb.append(", readTimeout=").append(readTimeout);
        sb.append(", keepAlive=").append(keepAlive);
        sb.append(", keepAliveIdleTime=").append(keepAliveIdleTime);
        sb.append(", keepAliveInterval=").append(keepAliveInterval);
        sb.append(", keepAliveRetries=").append(keepAliveRetries);
        sb.append(", trafficClass=").append(trafficClass);
        sb.append(", receiveBufferSize=").append(receiveBufferSize);
        sb.append(", sendBufferSize=").append(sendBufferSize);
        sb.append(", tcpNoDelay=").append(tcpNoDelay);
        sb.append(", tcpNagleThreshold=").append(tcpNagleThreshold);
        sb.append(", soReuseAddr=").append(soReuseAddr);
        sb.append(", soLinger=").append(soLinger);
        sb.append('}');
        return sb.toString();
    }
}
