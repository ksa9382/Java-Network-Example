package kr.co.direa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
	private static final Logger log = LogManager.getLogger(App.class);

	public static void main(String[] args) throws Exception {
		//        if (args.length < 2) {
		//            System.err.println(
		//                    "Usage: " + EchoClient.class.getSimpleName() +
		//                            " <Host> <Port>"
		//            );
		//            return;
		//        }

		//        String host = args[0];
		//        int port = Integer.parseInt(args[1]);

		String host = "127.0.0.1";
		int port = 38893;

		try {
			new EchoClient(host, port).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
