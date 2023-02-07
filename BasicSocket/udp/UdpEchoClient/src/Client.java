import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Random;

public class Client {
    static final int MAX_LENGTH = 1024;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("args = " + Arrays.toString(args) +
                    "\nUsage: java Client <ServerIP> <ServerPort>");
            System.exit(1);
        }

        // Create a datagram socket.
        try (DatagramSocket ds = new DatagramSocket()) {
            Random random = new Random();

            int loopCount = random.nextInt(10);
            System.out.println("loopCount = " + loopCount);

            while (--loopCount > 0) {
                int count = random.nextInt(10);

                int[] array = new int[count];
                for (int i = 0; i < count; i++)
                    array[i] = random.nextInt(100);

                // Convert int[] to byte[]
                byte[] buff = toByteArray(array);
                System.out.println("\nPacket: " + Arrays.toString(buff));

                // Determine destination address to sending datagram packet.
                DatagramPacket packet = new DatagramPacket(buff, 0, buff.length, InetAddress.getByName(args[0]), Integer.parseInt(args[1]));

                // Send packet to server
                ds.send(packet);

                // Create a buffer for receiving echo data.
                byte[] recvBuff = new byte[MAX_LENGTH];
                DatagramPacket recvPacket = new DatagramPacket(recvBuff, MAX_LENGTH);

                // Wait for receiving echo data.
                ds.receive(recvPacket);

                System.out.println("Received echo data..");
                System.out.println("Source Address: " + recvPacket.getSocketAddress());
                byte[] data = Arrays.copyOf(recvPacket.getData(), recvPacket.getLength());
                System.out.println("Packet: " + Arrays.toString(data));

                Thread.sleep(1000);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Convert int[] to byte[]
    public static byte[] toByteArray(int[] original) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(original.length * Integer.BYTES);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();

        intBuffer.put(original);

        return byteBuffer.array();
    }
}
