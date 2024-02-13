package org.gtkim.example;

import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

/**
 * Hello world!
 *
 */
@Slf4j
public class App
{
    public static void main( String[] args )
    {
        String workspace  = System.getProperty("user.dir");
        log.debug("Current Directory: [" + workspace + "]");

        Properties prop = loadProperties(workspace + File.separator + "resources" + File.separator + "application.properties");
        String remoteHostIp = prop.getProperty("remoteHostIp");
        int remoteHostPort = Integer.parseInt(prop.getProperty("remoteHostPort"));

        NettyHttpClient client = new NettyHttpClient();
        client.connect(remoteHostIp, remoteHostPort);
        client.createRequest(remoteHostIp, remoteHostPort, "");

        client.close();
    }

    private static Properties loadProperties(String path) {
        Properties prop = new Properties();

        try (BufferedReader bis = new BufferedReader(new FileReader(path))){
            prop.load(bis);
            log.debug("properties file loaded. [" + prop.toString() + "]");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return prop;
    }
}
