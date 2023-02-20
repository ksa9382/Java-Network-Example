package ByteBufTemp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * capacity 초과 시 maxCapacity까지 확장되는 지 테스트
 */
public class CapacityAutomaticExtentionTest {
    public static void main(String[] args) {
        try {
            ByteBuf limited = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, 5, 12);

            // (cap: 5/12)
            limited.writeBytes("Hello".getBytes(StandardCharsets.UTF_8));
            System.out.println("limited = " + limited.getCharSequence(0, limited.readableBytes(), StandardCharsets.UTF_8));

            // maxCapacity까지 자동확장 됨 (cap: 12/12)
            limited.writeBytes(", World".getBytes(StandardCharsets.UTF_8));
            System.out.println("limited = " + limited.getCharSequence(0, limited.readableBytes(), StandardCharsets.UTF_8));

            // capacity > maxCapacity 13/12
            limited.writeByte('.');
            System.out.println("limited = " + limited.getCharSequence(0, limited.readableBytes(), StandardCharsets.UTF_8));
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
