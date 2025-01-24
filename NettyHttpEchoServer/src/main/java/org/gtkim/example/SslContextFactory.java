package org.gtkim.example;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

public class SslContextFactory {
	public static SslContext createSslContext(final String path, final String password) throws Exception {
		String keyStoreFilePath = path;
		String keyStorePassword = password;

		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream(keyStoreFilePath), keyStorePassword.toCharArray());

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

		return SslContextBuilder.forServer(keyManagerFactory).build();
	}
}
