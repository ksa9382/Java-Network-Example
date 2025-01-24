package org.gtikim.nettywrap.tcp.initializer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {
    private final Set<ChannelHandler> handlerSet;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(handlerSet.toArray(ChannelHandler[]::new));
    }
}
