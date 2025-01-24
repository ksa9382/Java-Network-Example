package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class App {
	// private static final Logger log = LogManager.getLogger(App.class);

	// public static void main(String[] args) throws Exception {
	// 	Properties properties = new Properties();
	//
	// 	String workspace = System.getProperty("user.dir");
	// 	log.debug("Current Directory: [" + workspace + "]");
	//
	// 	String resourcePath = workspace + File.separator + ".." + File.separator + "resources" + File.separator
	// 		+ "application.properties";
	// 	try (FileInputStream fis = new FileInputStream(resourcePath);
	// 		 BufferedInputStream bis = new BufferedInputStream(fis)) {
	// 		properties.load(bis);
	// 		log.debug(properties);
	// 		log.debug(properties.getProperty("serverPort"));
	// 		int localPort = Integer.parseInt(properties.getProperty("serverPort"));
	//
	// 		AsyncTcpServer server = new AsyncTcpServer(localPort, new ServerHandler());
	// 		server.init();
	// 		server.run();
	// 	} catch (Exception e) {
	// 		log.error(e);
	// 	}
	// }

	public static void main(String[] args) throws Exception {
		// Create a server socket.
		try (ServerSocket serverSocket = new ServerSocket(15003)) {
			System.out.println("serverSocket = " + serverSocket);

			// Accept a request from client and establish connection.
			// This thread is blocked until a connection is made.
			Socket socket = serverSocket.accept();

			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

			while (true) {
				// Read message
				int availableLength = dis.available();
				int readOffset = 0;
				int fixedReadLength = 100;

				if (availableLength <= 0) {
					Thread.sleep(100); // 데이터가 올 때까지 대기
					continue;          // 다시 읽기 루프 시도
				}

				while (readOffset < availableLength) {
					byte[] readData = new byte[Math.min(fixedReadLength, availableLength - readOffset)];
					int currentReadLength = readData.length;

					int bytesRead = dis.read(readData, 0, currentReadLength);

					// 방어 코드 추가: 읽은 데이터가 없을 경우 루프 종료
					if (bytesRead == -1) {
						break;
					}

					readOffset += bytesRead;
					System.out.println("Client: " + new String(readData, 0, bytesRead));
					Thread.sleep(1000);
				}
				String message = dis.readUTF();
				System.out.println("Client: " + message);

				if (message.equals("quit"))
					break;
			}

			// Close all streams and socket.
			dis.close();
			dos.close();
			socket.close();

			System.out.println("Server socket is closed..");
		} catch (IOException e) {
			System.out.println("Server socket is aborted..");
			e.printStackTrace();
		}

		System.exit(0);
	}
}
