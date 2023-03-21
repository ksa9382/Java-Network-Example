package EmbeddedChannel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedLengthFrameDecoderTest {

    @Test
    void testDecode1() {
        // ByteBuf를 생성하고 9바이트를 저장
        ByteBuf buf = Unpooled.buffer(9);
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        // ByteBuf의 사본을 생성
        ByteBuf input = buf.duplicate();
        System.out.print("input.readableBytes() = " + input.readableBytes());
        System.out.println(", input.writableBytes() = " + input.writableBytes());

        // 임베디드 채널 생성하고, 생성자에 FixedFrameDecoder를 삽입
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        // 임베디드 채널에 바이트를 기록(Bytebuf.retain() 사용)
        assertTrue(channel.writeInbound(input.retain()));

        System.out.print("input.readableBytes() = " + input.readableBytes());
        System.out.println(", input.writableBytes() = " + input.writableBytes());

        // EmbeddedChannel.finish()를 호출(전체 인바운드 or 아웃바운드 메시지가 읽을 수 있는 경우 true 반환)
        assertTrue(channel.finish());

        // 임베디드 채널의 readInbound를 통해 ByteBuf 할당받음
        // 할당받은 ByteBuf는 3바이트 씩 들어있어야 함
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }

    @Test
    void testDecode2() {
        // ByteBuf를 생성하고 9바이트를 저장
        ByteBuf buf = Unpooled.buffer(9);
        for (int i = 0; i < 9; i++)
            buf.writeByte(i);

        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        assertFalse(channel.writeInbound(input.readBytes(2)));
        assertTrue(channel.writeInbound(input.readBytes(7)));
        assertTrue(channel.finish());

        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }
}