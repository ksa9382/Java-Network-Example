package EmbeddedChannel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AbsIntegerEncoderTest {

    @Test
    void testEncode() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; ++i)
            buf.writeInt(i * -1);

        EmbeddedChannel channel = new EmbeddedChannel(
                new AbsIntegerEncoder()
        );

        // ByteBuf를 기록하고 readOutbound가 데이터를 생성하는지 확인하는 어설션을 수행
        assertTrue(channel.writeOutbound(buf));
        channel.finish();

        // 바이트를 읽음
        for (int i = 1; i < 10; i++)
            assertEquals(i, (int) channel.readOutbound());
        assertNull(channel.readOutbound());
    }
}