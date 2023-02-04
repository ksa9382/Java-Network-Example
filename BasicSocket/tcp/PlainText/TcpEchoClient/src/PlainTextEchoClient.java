import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class PlainTextEchoClient {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("args = " + Arrays.toString(args));
            System.out.println("Usage: java Client <ServerIP> <Port>");
            System.exit(1);
        }

        try (Socket s = new Socket(args[0], Integer.parseInt(args[1]));
             Scanner sc = new Scanner(System.in)) {
            System.out.println("s.isConnected() = " + s.isConnected());
            System.out.println("Connected to server..");

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            while (true) {
                System.out.print("User: ");
                String message = sc.nextLine();

                dos.writeUTF(message);
                dos.flush();

                String echoMessage = dis.readUTF();
                System.out.println("Server: " + echoMessage);

                if (message.equals("quit"))
                    break;
            }

            dos.close();
            dis.close();

            System.out.println("Client socket is closed..");
        }
        catch (IOException e) {
            System.out.println("Client socket is aborted..");
            e.printStackTrace();
        }

        System.exit(0);
    }
}