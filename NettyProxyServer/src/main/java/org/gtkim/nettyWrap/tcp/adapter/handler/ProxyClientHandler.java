package org.gtkim.nettyWrap.tcp.adapter.handler;

import org.gtkim.ProxyServer;
import org.gtkim.example.nettyWrap.tcp.adapter.AsyncTcpClient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class ProxyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	//    private static final Logger log = LogManager.getLogger(ProxyClientHandler.class);
	private final ProxyServer parent;

	//    public ProxyClientHandler(ProxyServer parent) {
	//        this.parent = parent;
	//    }

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		log.info("Connected to proxy[" + ctx.channel().remoteAddress() + "]");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		log.info("Disconnected from proxy[" + ctx.channel().remoteAddress() + "]");
	}

	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
		ByteBuf copied = Unpooled.copiedBuffer(byteBuf);
		byte[] rawData = new byte[copied.readableBytes()];
		copied.readBytes(rawData);

		String data = new String(rawData);

		log.info("Received response: [" + rawData.length + " bytes], Data: [" + data + "]");

		AsyncTcpClient client = parent.getClient();

		if (!client.isConnected()) {
			log.error("Client is not opened.");
			return;
		}

		client.send(rawData);

		log.info("Success to send to remote host. [" + client.getRemoteIp() + ":" + client.getRemotePort() + "]");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		log.error("Unhandled Exception. [" + cause.getCause() + "]");
	}
}
