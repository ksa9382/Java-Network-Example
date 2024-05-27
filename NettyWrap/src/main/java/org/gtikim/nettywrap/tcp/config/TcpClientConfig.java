package org.gtikim.nettywrap.tcp.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class TcpClientConfig {
    @Value("${netty.channel.remoteIp}")
    private String remoteIp;

    @Value("${netty.channel.remotePort}")
    private int remotePort;

    @Value("${netty.channel.connectTimeout}")
    private int connectTimeout;

    @Value("${netty.channel.readTimeout}")
    private int readTimeout;

    @Value("${netty.channel.keepAlive}")
    private boolean keepAlive;

    @Value("${netty.channel.keepAliveIdleTime}")
    private int keepAliveIdleTime;

    @Value("${netty.channel.keepAliveInterval}")
    private int keepAliveInterval;

    @Value("${netty.channel.keepAliveRetries}")
    private int keepAliveRetries;

    @Value("${netty.channel.trafficClass}")
    private int trafficClass;

    @Value("${netty.channel.receiveBufferSize}")
    private int receiveBufferSize;

    @Value("${netty.channel.sendBufferSize}")
    private int sendBufferSize;

    @Value("${netty.channel.tcpNoDelay}")
    private boolean tcpNoDelay;

    @Value("${netty.channel.tcpNagleThreshold}")
    private int tcpNagleThreshold;

    @Value("${netty.channel.soReuseAddr}")
    private boolean soReuseAddr;

    @Value("${netty.channel.soLinger}")
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
