package org.gtkim;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

import org.gtkim.nettyWrap.tcp.adapter.handler.ProxyServerHandler;
import org.gtkim.nettyWrap.tcp.adapter.response.S2ATransactionResponseCreator;

import lombok.extern.log4j.Log4j2;

/**
 * Hello world!
 *
 */
@Log4j2
public class App {
	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();

		log.debug("Current Directory: [" + System.getProperty("user.dir") + "]");
		BufferedInputStream bis = new BufferedInputStream(
			new FileInputStream("src/main/resources/application.properties"));
		try {
			properties.load(bis);
			log.info(String.valueOf(properties));
			log.info(properties.getProperty("serverPort"));
			int localPort = Integer.parseInt(properties.getProperty("serverPort"));

			String clientIp = properties.getProperty("clientIp");
			int clientPort = Integer.parseInt(properties.getProperty("clientPort"));

			ProxyServer server = new ProxyServer(localPort,
				new ProxyServerHandler(new S2ATransactionResponseCreator()));

			server.initServer()
				.readyClient(clientIp, clientPort)
				.run();

		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
}