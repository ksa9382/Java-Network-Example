package org.gtkim.nettyWrap.tcp.adapter.handler;

import org.gtkim.ProxyServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	//    private static final Logger log = LogManager.getLogger(ClientHandler.class);
	private final ProxyServer parent;

	//    public ClientHandler(ProxyServer parent) {
	//        super();
	//        this.parent = parent;
	//    }

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		log.debug("Connected to remote host. [" + ctx.channel().remoteAddress() + "]");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		log.debug("Disconnected from remote host. [" + ctx.channel().remoteAddress() + "]");

		// while (!parent.reconnectClient()) {
		// 	log.debug("retry connect.");
		// 	Thread.sleep(3000);
		// }
	}

	protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
}
