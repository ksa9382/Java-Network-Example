package org.gtkim.nettyWrap.tcp.adapter.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gtkim.example.nettyWrap.tcp.adapter.AsyncTcpClient;

@Slf4j
@RequiredArgsConstructor
public class AsyncClientRun implements Runnable{
//    private static final Logger log = LogManager.getLogger(AsyncClientRun.class);
    private final AsyncTcpClient client;

//    public AsyncClientRun(AsyncTcpClient client) {
//        this.client = client;
//    }

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
