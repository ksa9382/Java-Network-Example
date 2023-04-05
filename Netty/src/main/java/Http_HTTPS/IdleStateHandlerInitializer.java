package Http_HTTPS;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 원격 피어에 하트비트 메시지를 보내는 일반적인 방법을 통해
 * 60초 동안 수신한 데이터가 없을 경우 알림을 받음
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        // ChannelPipeline 참조 획득
        ChannelPipeline pipeline = ch.pipeline();

        // IdleStateHandler: 트리거 될 때, IdleStateEvent를 전송
        pipeline.addLast(new IdleStateHandler(
                0, 0, 60, TimeUnit.SECONDS));
        pipeline.addLast(new HeartbeatHandler());
    }

    public static final class HeartbeatHandler
    extends ChannelInboundHandlerAdapter {
        // 상수 버퍼 생성
        private static final ByteBuf HEARTBEAT_SEQUENCE =
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
                        "HEARTBEAT", CharsetUtil.ISO_8859_1));

        /**
         * 트리거 된 evt가 IdleStateEvent이면 하트비트 메시지를 전송하고,
         * 전송이 실패하면 연결을 닫는 방식으로 핸들링하며,
         * 아닐 경우 디폴트 버전을 통해 핸들링
         * @param ctx 채널 핸들러 컨텍스트
         * @param evt 트리거 된 이벤트
         * @throws Exception
         */
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
                        .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                super.userEventTriggered(ctx,evt);
            }
        }
    }
}