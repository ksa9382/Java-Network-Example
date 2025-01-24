package org.gtkim;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
	private static final Logger log = LogManager.getLogger(App.class);

	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();

		String workspace = System.getProperty("user.dir");
		log.debug("Current Directory: [" + workspace + "]");

		// String resourcePath = workspace + File.separator + ".." + File.separator + "resources" + File.separator
		// 	+ "application.properties";

		String resourcePath = "C:\\Sources\\2. IntelliJWorkspace\\Java-Network-Example\\NettyEchoServer\\src\\main\\resources\\application.properties";

		// String resourcePath =
		// 	"C:\\Sources\\2. IntelliJWorkspace\\Java-Network-Example\\NettyEchoServer\\src\\main\\resources"
		// 		+ File.separator
		// 		+ "application.properties";

		try (FileInputStream fis = new FileInputStream(resourcePath);
			 BufferedInputStream bis = new BufferedInputStream(fis)) {
			properties.load(bis);
			log.debug("properties file is loaded.. [" + properties + "]");
			int localPort = Integer.parseInt(properties.getProperty("serverPort"));

			EchoServer server = new EchoServer(localPort, new EchoServerHandler());
			server.start();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}