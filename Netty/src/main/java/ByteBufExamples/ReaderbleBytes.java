package ByteBufExamples;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledHeapByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ReaderbleBytes {
    private static final byte[] SAMPLE_STR_BYTES = "HELLO_WORLD".getBytes(StandardCharsets.UTF_8);

    public static void main(String[] args) {
        ByteBuf buf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, 1024, 1024);

        // 최초 상태
        printBufReadableAndWritableStatus(buf);

        // 쓰기 이후 상태
        writeAndLog(buf, SAMPLE_STR_BYTES);
        printBufReadableAndWritableStatus(buf);

        // 읽기 이후 상태
        readAndLog(buf);
        printBufReadableAndWritableStatus(buf);

        // 복사 전
        buf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, 1024, 1024);
        writeAndLog(buf, SAMPLE_STR_BYTES);
        ByteBuf tgtBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, 1024, 1024);

        printBufReadableAndWritableStatus(buf);
        printBufReadableAndWritableStatus(tgtBuf);

        readAndWrite(buf, tgtBuf);

        // 복사 후
        System.out.println("src: ");
        printBufReadableAndWritableStatus(buf);
        System.out.println("tgt: ");
        printBufReadableAndWritableStatus(tgtBuf);
    }

    private static void readAndLog(ByteBuf buf) {
        if (!buf.isReadable())
            return;

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        while (buf.isReadable()) {
            builder.append(buf.readByte());
            if (buf.isReadable())
                builder.append(", ");
        }
        builder.append("]");

        System.out.println(builder);
    }

    public static void readAndWrite(ByteBuf src, ByteBuf tgt) {        while (src.isReadable()) {
            // 목적지 ByteBuf 객체의 쓰기 가능한 바이트 만큼 읽음
            // 예외 발생 버전
//            {
//                // writableBytes= capacity
//                // Transfers this buffer's data to the specified destination starting at the current readerIndex until the destination becomes non-writable, and increases the readerIndex by the number of the transferred bytes.
//                // 만약, tgt의 writableBytes만큼 읽을 수 없다면, IndexOutOfBoundsException 발생
//                src.readBytes(tgt);
//            }

            // 개선 버전
            {
                src.readBytes(tgt, tgt.writerIndex(), src.readableBytes());
            }
        }
    }

    private static void writeAndLog(ByteBuf buf, byte[] arr) {
        buf.writeBytes(arr);
        System.out.println("write " + Arrays.toString(arr) + ", " + arr.length + "bytes to buf.\n");
    }

    private static void printBufReadableAndWritableStatus(ByteBuf buf) {
        System.out.println("buf.readerIndex() = " + buf.readerIndex());
        System.out.println("buf.readableBytes() = " + buf.readableBytes());
        System.out.println("buf.writerIndex() = " + buf.writerIndex());
        System.out.println("buf.writableBytes() = " + buf.writableBytes() + "\n");
    }

    public static class ByteBufSlice {
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

    /**
     * capacity 초과 시 maxCapacity까지 확장되는 지 테스트
     */
    public static class CapacityAutomaticExtentionTest {
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
}