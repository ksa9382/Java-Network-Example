package kr.co.direa;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App 
{
    private static final Logger log = LogManager.getLogger(App.class);

    public static void main( String[] args ) throws Exception
    {
        if (args.length < 2) {
            System.err.println(
                    "Usage: " + EchoClient.class.getSimpleName() +
                            " <Host> <Port>"
            );
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            new EchoClient(host, port).start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
