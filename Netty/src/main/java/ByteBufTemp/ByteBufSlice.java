package ByteBufTemp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class ByteBufSlice {
    public static void main(String[] args) {
        // 지정한 문자열 바이트를 저장하는 버퍼 생성
        ByteBuf buf = Unpooled.copiedBuffer("Hello, World", StandardCharsets.UTF_8);
        ByteBuf sliced = buf.slice(0, 5);   // Hello 부분 슬라이스
        System.out.println("sliced = " + sliced);

        buf.setByte(0, (byte)'C');
        assert buf.getByte(0) == sliced.getByte(0);

        ByteBuf copied = buf.copy(0, 5);
        System.out.println("copied = " + copied);
        buf.setByte(0, 'H');
        assert buf.getByte(0) != copied.getByte(0);
    }
}
