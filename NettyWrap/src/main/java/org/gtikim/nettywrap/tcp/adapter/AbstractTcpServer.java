package org.gtikim.nettywrap.tcp.adapter;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.gtikim.nettywrap.tcp.config.TcpServerConfig;
import org.gtikim.nettywrap.tcp.initializer.TcpServerInitializer;
import org.springframework.stereotype.Component;

@Log4j2
@Getter
@RequiredArgsConstructor
@Component
public class AbstractTcpServer implements ITcpServer {
    private final TcpServerConfig config;
    private final TcpServerInitializer initializer;

    private ServerBootstrap bootstrap = new ServerBootstrap();

    // EventLoopGroup 생성
    EventLoopGroup boss = new NioEventLoopGroup(1);
    // EventLoopGroup 생성
//    EventLoopGroup group = new NioEventLoopGroup(config.getWorkThread());
    EventLoopGroup group = new NioEventLoopGroup(10);

    @Override
    public void startup() {
    }
}
