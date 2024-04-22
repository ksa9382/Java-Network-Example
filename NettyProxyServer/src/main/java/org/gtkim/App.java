package org.gtkim;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gtkim.nettyWrap.tcp.adapter.handler.ProxyServerHandler;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger log = LogManager.getLogger(App.class);
    public static void main( String[] args ) throws Exception
    {
        Properties properties = new Properties();

        log.debug("Current Directory: [" + System.getProperty("user.dir") + "]");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(".\\resources\\application.properties"));
        try {
            properties.load(bis);
            log.debug(properties);
            log.debug(properties.getProperty("serverPort"));
            int localPort = Integer.parseInt(properties.getProperty("serverPort"));

            String clientIp = properties.getProperty("clientIp");
            int clientPort = Integer.parseInt(properties.getProperty("clientPort"));

            String proxyIp = properties.getProperty("proxyTargetIp");
            int proxyPort = Integer.parseInt(properties.getProperty("proxyTargetPort"));

            ProxyServer server = new ProxyServer(localPort, new ProxyServerHandler());
            server.initServer()
                    .readyProxy(proxyIp,proxyPort)
                    .readyClient(clientIp, clientPort)
                    .run();

        }catch (Exception e) {
            throw e;
        }

    }
}
