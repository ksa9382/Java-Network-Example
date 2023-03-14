package EmbeddedChannel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

class FixedLengthFrameDecoderTest {

    @Test
    public void testDecode() {
        ByteBuf buf = Unpooled.buffer();

        for (int i = 0; i < 9; i++)
            buf.writeByte(i);

        ByteBuf input = buf.duplicate();
    }
}