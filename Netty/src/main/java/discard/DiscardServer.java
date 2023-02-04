package discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {
    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();     // Accept incoming connection.
        EventLoopGroup workerGroup = new NioEventLoopGroup();   // Handle the traffic of the accepted connection
                                                                // and registers the accepted connection to the worker.

        try {
            ServerBootstrap b = new ServerBootstrap();  // set up a server
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)  // Instantiate a new channel to accept incoming connections.
                    .childHandler(new ChannelInitializer<SocketChannel>() {     // Configure
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // set the parameters for the NioServerSocketChannel that accepts incoming connections.
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // set the parameters for the Channels(NioSocketChannel) accepted by the parent ServerChannel.

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 56401;
        if (args.length > 0)
            port = Integer.parseInt(args[0]);

        new DiscardServer(port).run();
    }
}
