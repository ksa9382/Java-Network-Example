import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class PlainTextEchoServer {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("args = " + Arrays.toString(args));
            System.out.println("Usage: java Server <Port>");
            System.exit(1);
        }

        // Create a server socket.
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]))) {
            System.out.println("serverSocket = " + serverSocket);

            // Accept a request from client and establish connection.
            // This thread is blocked until a connection is made.
            Socket socket = serverSocket.accept();

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            while (true) {
                // Read message
                String message = dis.readUTF();
                System.out.println("Client: " + message);

                // Echo message
                dos.writeUTF(message);
                dos.flush();

                if (message.equals("quit"))
                    break;
            }

            // Close all streams and socket.
            dis.close();
            dos.close();
            socket.close();

            System.out.println("Server socket is closed..");
        }
        catch (IOException e) {
            System.out.println("Server socket is aborted..");
            e.printStackTrace();
        }

        System.exit(0);
    }
}
