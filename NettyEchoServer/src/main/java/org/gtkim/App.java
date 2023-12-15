package org.gtkim;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger log = LogManager.getLogger(App.class);
    public static void main( String[] args ) throws Exception
    {
        if (args.length != 1) {
            log.error("프로그램 실행인수 : 포트필요");
            System.exit(1);
        }

        int port = 0;
        port = Integer.parseInt(args[0]);

        try {
            new EchoServer(port).start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
