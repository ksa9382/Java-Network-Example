package discard;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

public class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                            // 다음 ChannelOutboundHandler로 전달되지 않는 경우
        ReferenceCountUtil.release(msg);    // ReferenceCountUtil.release()를 이용해 리소스를 해제
                                            // 전송 레이어에 도달한 메시지는 write될 때 또는 Channel이 닫힐 때 자동으로 해제됨

        promise.setSuccess();               // 데이터 정상처리를 ChannelPromise에 알림
    }
}
