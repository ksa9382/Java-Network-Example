package org.gtkim.nettyWrap.tcp.adapter.handler;

import org.gtkim.example.nettyWrap.tcp.adapter.AsyncTcpClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class AsyncClientRun implements Runnable {
	private final AsyncTcpClient client;

	public void run() {
		try {
			while (true) {
				if (!client.isConnected())
					client.init();

				Thread.sleep(1000);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
