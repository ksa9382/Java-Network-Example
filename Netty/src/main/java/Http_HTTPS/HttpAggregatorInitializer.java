package Http_HTTPS;

import com.sun.net.httpserver.HttpServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * HTTP 메시지 조각을 자동으로 집계
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {
    private boolean isClient;

    public HttpAggregatorInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline cp = ch.pipeline();

        if (isClient) {
            // HttpClientCodec = CombinedChannelDuplexHandler<HttpResponseDecoder, HttpRequestEncoder> 상속
            cp.addLast("codec", new HttpClientCodec());
        }
        else {
            // HttpServerCodec = CombinedChannelDuplexHandler<HttpRequestDecoder, HttpResponseEncoder> 상속
            cp.addLast("codec", new HttpServerCodec());
        }

        // 최대 메시지 크기를 512KB로 지정하고 HttpObjectAggregator를 파이프라인에 추가
        cp.addLast("aggregator",
                new HttpObjectAggregator(512 * 1024));
    }
}
