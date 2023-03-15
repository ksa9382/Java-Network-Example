package EmbeddedChannel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class FixedLengthFrameDecoderTest {

    @Test
    public void testDecode() {
        ByteBuf buf = Unpooled.buffer();

        for (int i = 0; i < 9; i++)
            buf.writeByte(i);

        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(
                new FixedLengthFrameDecoder(3));

        // ByteBuf에 대한 참조를 증가시키면서 channel.writeInbound에 삽입
        // writeInbound에 삽입 시, readInbound() 메서드로 데이터를 읽기 가능하면 true 반환
        assertTrue(channel.writeInbound(input.retain()));

        // 채널을 완료로 표시
        assertTrue(channel.finish());

        // 메시지를 읽음
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }

    @Test
    public void testDecode2() {
        ByteBuf buf = Unpooled.buffer();

        for (int i = 0; i < 9; i++)
            buf.writeByte(i);

        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(
                new FixedLengthFrameDecoder(3)
        );

        assertFalse(channel.writeInbound(input.readBytes(2)));
        assertTrue(channel.writeInbound(input.readBytes(1)));
        assertTrue(channel.writeInbound(input.readBytes(6)));
        assertTrue(channel.finish());

        ByteBuf readBuf = (ByteBuf) channel.readInbound();    // 전체 ChannelPipeline을 통과하여 도착한 인바운드 메시지를 반환
        assertEquals(buf.readSlice(3), readBuf);
        readBuf.release();

        readBuf = (ByteBuf) channel.readInbound();    // 총 9바이트를 보냈어도, 디코더 때문에 3개씩 들어있음
        assertEquals(buf.readSlice(3), readBuf);
        readBuf.release();

        readBuf = (ByteBuf) channel.readInbound();    // 총 9바이트를 보냈어도, 디코더 때문에 3개씩 들어있음
        assertEquals(buf.readSlice(3), readBuf);
        readBuf.release();
    }
}