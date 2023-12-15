package org.gtkim;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gtkim.nettyWrap.tcp.adapter.handler.LoadBalanceHandler;

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
            int localPort = Integer.parseInt(properties.getProperty("serverPort"));
            int mode = Integer.parseInt(properties.getProperty("mode"));


            LoadBalanceServer server = new LoadBalanceServer(localPort, new LoadBalanceHandler(mode));
            server.init()
                    .run();
        } catch (Exception e) {
            log.error(e);
        }
    }
}
