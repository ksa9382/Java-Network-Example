package EmbeddedChannel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedChunkDecoderTest {

    @Test
    void testDecode() {
        ByteBuf buf = Unpooled.buffer(9);
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        ByteBuf input = buf.duplicate();
        EmbeddedChannel ch = new EmbeddedChannel(new FixedChunkDecoder(3));

        assertTrue(ch.writeInbound(input.readBytes(2)));
        try {
            ch.writeInbound(input.readBytes(4));
            Assert.fail();  // 예외가 발생하지 않을 경우, 테스트 실패
        }
        catch (TooLongFrameException e) {
            e.printStackTrace();
        }

        assertTrue(ch.writeInbound(input.readBytes(3)));
        assertTrue(ch.finish());

        ByteBuf read = (ByteBuf) ch.readInbound();
        assertEquals(buf.readSlice(2), read);
        read.release();

        read = (ByteBuf) ch.readInbound();
        assertEquals(buf.skipBytes(4).readSlice(3), read);
        read.release();
    }
}