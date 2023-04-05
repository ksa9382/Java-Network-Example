package Http_HTTPS;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 *
 */
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {
    private final SslContext ctx;
    private final boolean isClient;

    public HttpsCodecInitializer(SslContext ctx, boolean isClient) {
        this.ctx = ctx;
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline cp = ch.pipeline();
        SSLEngine engine = ctx.newEngine(ch.alloc());
        cp.addFirst("ssl", new SslHandler(engine));

        if (isClient) {
            cp.addLast("codec", new HttpClientCodec());
        }
        else {
            cp.addLast("codec", new HttpServerCodec());
        }
    }
}
