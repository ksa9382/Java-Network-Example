package Http_HTTPS;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * HTTP 메시지를 자동으로 압축
 */
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {
    private boolean isClient;

    public HttpCompressionInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline cp = ch.pipeline();

        if (isClient) {
            cp.addLast("codec", new HttpClientCodec());
            // 클라이언트인 경우 서버에서 받은 압축된 콘텐츠를 처리할 HttpContentDecompressor를 추가
            cp.addLast("decompressor",
                    new HttpContentDecompressor());
        }
        else {
            cp.addLast("codec", new HttpServerCodec());
            // 서버인 경우 데이터를 압축할 HttpContentCompressor를 추가
            cp.addLast("compressor",
                    new HttpContentCompressor());
        }
    }
}
