package Bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class UseChannelInitializer {
    public static void main(String[] args) {
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
//                .childHandler(new ChannelInitializerImpl())
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
                    }
                });

        // Local Class는 사용 구문보다 위에 정의되어 있어야 함
//        final class ChannelInitializerImpl extends ChannelInitializer<Channel> {
//            @Override
//            protected void initChannel(Channel channel) throws Exception {
//                channel.pipeline()
//                        .addLast(new HttpClientCodec())
//                        .addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
//            }
//        }
    }
}
