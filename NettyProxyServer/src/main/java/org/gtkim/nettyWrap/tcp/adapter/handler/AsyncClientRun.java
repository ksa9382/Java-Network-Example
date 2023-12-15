package org.gtkim.nettyWrap.tcp.adapter.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gtkim.nettyWrap.tcp.adapter.AsyncTcpClient;

public class AsyncClientRun implements Runnable{
    private static final Logger log = LogManager.getLogger(AsyncClientRun.class);
    private AsyncTcpClient client;

    public AsyncClientRun(AsyncTcpClient client) {
        this.client = client;
    }

    public void run() {
        try {
            while (true) {
                if (!client.isConnected())
                    client.init();

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}
