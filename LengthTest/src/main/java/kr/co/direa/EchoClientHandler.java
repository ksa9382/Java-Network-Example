package kr.co.direa;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connected to server..");
        // 활성화 시 메시지 전송(예제용)
        String data = "KLMJPONLRKL0200124202308091540469791929L41L00      00460         0        0              000                  20110879050015Y20220120221206          0000000";
        ctx.writeAndFlush(Unpooled.copiedBuffer(data,
                Charset.forName("EUC-KR")));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Client received: " +
                byteBuf.toString(Charset.forName("EUC-KR")));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
