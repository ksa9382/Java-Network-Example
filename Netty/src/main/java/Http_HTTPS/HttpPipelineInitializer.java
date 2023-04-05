package Http_HTTPS;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * HTTP 지원을 추가
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {
    private final boolean isClient;

    public HttpPipelineInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline chPipeline = ch.pipeline();

        // Client: HttpRequestEncoder와 HttpResponseDecoder가 필요
        if (isClient) {
            chPipeline.addLast("encoder", new HttpRequestEncoder());
            chPipeline.addLast("decoder", new HttpResponseDecoder());
        }
        else {
            chPipeline.addLast("encoder", new HttpResponseEncoder());
            chPipeline.addLast("decoder", new HttpRequestDecoder());
        }
    }
}
