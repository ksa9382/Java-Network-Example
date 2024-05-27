package org.gtikim.nettywrap.tcp.adapter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.gtikim.nettywrap.tcp.config.TcpClientConfig;
import org.gtikim.nettywrap.tcp.handler.TcpClientInitializer;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
@Component
public class AbstractTcpClient implements ITcpClient {
    private final TcpClientConfig config;
    private final TcpClientInitializer initializer;
    private final EventLoopGroup worker = new NioEventLoopGroup();

    private Optional<Channel> channel;

    @PostConstruct
    public void startup() {
        log.info("tcpConfiguration: [" + config.toString() + "]");

        try {
            Bootstrap bootstrap = createBootstrap();

            ChannelFuture channelFuture = bootstrap.connect();
            channelFuture.awaitUninterruptibly();

            if (channelFuture.isSuccess()) {
                channel = Optional.ofNullable(channelFuture.channel());
            } else if (channelFuture.isCancelled()) {
                // TODO
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private Bootstrap createBootstrap() throws Exception{
        return new Bootstrap()
                .group(worker)
                .channel(NioSocketChannel.class)
                .remoteAddress(config.getRemoteIp(), config.getRemotePort())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeout() * 1000)
                .option(ChannelOption.SO_TIMEOUT, config.getReadTimeout())
                .option(ChannelOption.TCP_NODELAY, config.isTcpNoDelay())
                .option(ChannelOption.SO_KEEPALIVE, config.isKeepAlive())
                .option(ChannelOption.SO_REUSEADDR, config.isSoReuseAddr())
                .option(ChannelOption.SO_LINGER, config.getSoLinger())
                .option(ChannelOption.RCVBUF_ALLOCATOR,
                        new FixedRecvByteBufAllocator(config.getReceiveBufferSize())
                ).handler(initializer);
    }

    @Override
    public void send(String message) {
        channel.ifPresent(channel -> {
            channel.writeAndFlush(message);
        });
    }

    @PreDestroy
    public void close() {
        log.info("closed");

        try {
            worker.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
