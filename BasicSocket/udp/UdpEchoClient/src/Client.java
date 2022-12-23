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

            int loopCount = random.nextInt(1, 10);

            while ((--loopCount) > 0) {
                int count = random.nextInt(0, 10);

                int[] array = new int[count];
                for (int i = 0; i < count; i++)
                    array[i] = random.nextInt(0, 100);

                // Convert int[] to byte[]
                ByteBuffer byteBuffer = ByteBuffer.allocate(Math.min(count * 4, MAX_LENGTH));
                IntBuffer intBuffer = byteBuffer.asIntBuffer();
                intBuffer.put(array);

                byte[] buff = byteBuffer.array();
                System.out.println("Packet: " + Arrays.toString(buff));

                // Determine datagram packet
                DatagramPacket packet = new DatagramPacket(buff, 0, buff.length);
                packet.setAddress(InetAddress.getByName(args[0]));
                packet.setPort(Integer.parseInt(args[1]));

                // Send packet to server
                ds.send(packet);

                byte[] recvBuff = new byte[MAX_LENGTH];
                DatagramPacket recvPacket = new DatagramPacket(recvBuff, MAX_LENGTH);
                ds.receive(recvPacket);

                System.out.println("recvPacket.getSocketAddress() = " + recvPacket.getSocketAddress());
                System.out.println("Received packet: " + Arrays.toString(recvPacket.getData()));

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
}
