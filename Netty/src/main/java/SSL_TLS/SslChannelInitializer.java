package SSL_TLS;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 *
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean startTls;

    /**
     *
     * @param context 이용할 SslContext를 전달
     * @param startTls true인 경우 처음 기록된 메시지가 암호화되지 않음
     *                 클라이언트는 true로 설정해야 함
     */
    public SslChannelInitializer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // 각 SslHandler 인스턴스마다 Channel의 ByteBufAllocator를 이용해 SslContext에서 새로운 SSLEngine을 얻음
        SSLEngine engine = context.newEngine(ch.alloc());
        ch.pipeline().addFirst("ssl",
                new SslHandler(engine, startTls));
    }
}