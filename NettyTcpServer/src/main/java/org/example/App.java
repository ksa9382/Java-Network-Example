package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gtkim.example.nettyWrap.tcp.adapter.AsyncTcpServer;

import java.io.BufferedInputStream;
import java.io.File;
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

        String workspace  = System.getProperty("user.dir");
        log.debug("Current Directory: [" + workspace + "]");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(workspace + File.separator + "resources" + File.separator + "application.properties"));
        try {
            properties.load(bis);
            log.debug(properties);
            log.debug(properties.getProperty("serverPort"));
            int localPort = Integer.parseInt(properties.getProperty("serverPort"));

            AsyncTcpServer server = new AsyncTcpServer(localPort, new ServerHandler());
            server.init();
            server.run();
        }catch (Exception e) {
            log.error(e);
        }
    }
}
