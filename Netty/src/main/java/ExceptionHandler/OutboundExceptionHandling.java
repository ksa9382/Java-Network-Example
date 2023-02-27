package ExceptionHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class OutboundExceptionHandling {
    public static void main(String[] args) {
        Channel channel = null; // null 초기화지만, 참조 대상이 있다고 가정
        ChannelFuture future = channel.write("Some Message");
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (!f.isSuccess()) {
                    f.cause().printStackTrace();
                    f.channel().close();
                }
            }
        });
    }
}