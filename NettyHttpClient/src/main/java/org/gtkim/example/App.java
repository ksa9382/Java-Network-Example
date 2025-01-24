package org.gtkim.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
	public static void main(String[] args) throws Exception {
		String workspace = System.getProperty("user.dir");
		log.debug("Current Directory: [" + workspace + "]");

		Properties prop = loadProperties(
			workspace + File.separator + "resources" + File.separator + "application.properties");
		log.debug("prop: [" + prop + "]");

		String remoteHostIp = prop.getProperty("remoteHostIp");
		int remoteHostPort = Integer.parseInt(prop.getProperty("remoteHostPort"));
		String version = prop.getProperty("version");
		String method = prop.getProperty("method");
		String contentType = prop.getProperty("contentType");
		String tgrmContent = prop.getProperty("tgrmContent");
		String encoding = prop.getProperty("tgrmEncoding");

		NettyHttpClient client = new NettyHttpClient();

		client.connect(remoteHostIp, remoteHostPort);
		HttpRequest request = new HttpRequestBuilder()
			.version(version)
			.method(method)
			.contentType(contentType)
			.content(tgrmContent, encoding)
			.build();

		client.send(request, false);
		client.close();
	}

	private static Properties loadProperties(String path) {
		Properties prop = new Properties();

		try (BufferedReader bis = new BufferedReader(new FileReader(path))) {
			prop.load(bis);
			log.debug("properties file loaded. [" + prop + "]");
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return prop;
	}
}
