package Decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * ReplayingDecoder의 형식 매개변수 S는 상태 관리에 사용
 * Void 지정 시 아무 작업도 하지 않음
 * ReplayingDecoder는 ByteToMessageDecoder보다 느림
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {

    /**
     *
     * @param ctx           the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in            ReplayingDecoderBuffer, readInt() 메서드는 읽을 바이트가 충분치 않은 경우 기본 클래스에서 포착하고
     *                      처리할 수 있는 에러를 생성
     * @param out           the {@link List} to which decoded messages should be added
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(in.readInt());
    }
}
