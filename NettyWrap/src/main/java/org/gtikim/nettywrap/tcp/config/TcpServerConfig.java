package org.gtikim.nettywrap.tcp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "netty.channel.server")
public class TcpServerConfig {
    // TCP 수신 포트
    private int port;

    // 작업 쓰레드 수
    private int workThread;

    // TCP 연결 타임아웃 (초)
    private int connectionTimeout;

    // 읽기 타임아웃 (초)
    private int readTimeout;

    // TCP Keep-Alive 설정
    private boolean keepAlive;

    // TCP Keep-Alive Idle 시간 (초)
    private int keepAliveIdleTime;

    // TCP Keep-Alive Interval 시간 (초)
    private int keepAliveInterval;

    // TCP Keep-Alive 재전송 시도 횟수
    private int keepAliveRetries;

    // TCP 부하 분산 알고리즘
    private int trafficClass;

    // TCP 수신 버퍼 크기 (바이트)
    private int receiveBufferSize;

    // TCP 전송 버퍼 크기 (바이트)
    private int sendBufferSize;

    // TCP Nagle 알고리즘 사용 여부
    private boolean tcpNoDelay;

    // TCP Nagle 알고리즘과 함께 사용할 수 있는 최소 예약 크기 (바이트)
    private int tcpNagleThreshold;

    // TCP 연결 재사용 여부
    private boolean soReuseAddr;

    // SO_KEEPALIVE 옵션 사용 여부
    private boolean soKeepAlive;

    // TCP 서버 백로그 크기
    private int backlog;

    // TCP 서버 소켓 재사용 포트 여부
    private boolean soReusePort;
}
