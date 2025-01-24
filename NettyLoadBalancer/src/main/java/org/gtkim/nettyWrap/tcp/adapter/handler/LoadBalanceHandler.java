package org.gtkim.nettyWrap.tcp.adapter.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gtkim.LoadBalanceServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class LoadBalanceHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LogManager.getLogger(LoadBalanceHandler.class);
	private final int mode;   // 0: 정방향, 1: 역방향
	private LoadBalanceServer parent;

	public LoadBalanceHandler(int mode) {
		this.mode = mode;
	}

	public void bindParent(LoadBalanceServer parent) {
		this.parent = parent;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		log.debug("remote host[" + ctx.channel().remoteAddress() + "] is connected.");

		String remoteAddr = ctx.channel().remoteAddress().toString();
		Channel finded = parent.findSession(remoteAddr);
		if (!parent.isEnrolled(remoteAddr))
			parent.enrollSession(remoteAddr, ctx.channel());

		ctx.pipeline().addLast(new ExceptionHandler());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);

		String remoteAddr = ctx.channel().remoteAddress().toString();
		log.debug("remote host[" + remoteAddr + "] is disconnected.");

		parent.deleteSession(remoteAddr);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String remoteAddr = ctx.channel().remoteAddress().toString();

		ByteBuf buf = Unpooled.copiedBuffer((ByteBuf)msg);
		byte[] rawData = new byte[buf.readableBytes()];
		buf.readBytes(rawData);
		String data = new String(rawData);

		log.debug("Received data[" + data + "] from remote host[" + remoteAddr + "].");

		int index = 0;
		String length = data.substring(index, index + 4);
		index += 4;
		log.debug("length = " + length);

		String tranCode = data.substring(index, index + 9);
		index += 9;
		log.debug("tranCode = " + tranCode);

		String chnIntfId = data.substring(index, index + 11);
		index += 11;
		log.debug("chnIntfId = " + chnIntfId);

		String msgNo = data.substring(index, index + 4);
		index += 4;
		log.debug("msgNo = " + msgNo);

		String msgCode = data.substring(index, index + 6);
		index += 6;
		log.debug("msgCode = " + msgCode);

		String filler = data.substring(index, index + 9);
		index += 9;
		log.debug("filler = " + filler);

		String tranUnqNo = data.substring(index, index + 12);
		index += 12;
		log.debug("tranUnqNo = " + tranUnqNo);

		String filler2 = data.substring(index, index += 9);
		log.debug("filler2 = " + filler2);

		// TODO: 전문처리
		ByteBuf response;
		log.debug("요청 수신");
		length = "0060";
		String dummy = chnIntfId.substring(0, 6);
		msgNo = "0210";
		filler = "99999999999999999999999";
		filler2 = "999999999";

		StringBuilder sb = new StringBuilder();
		sb.append(length).append(dummy).append(msgNo).append(msgCode).append(filler).append(tranUnqNo).append(filler2);

		byte[] resRawData = sb.toString().getBytes();

		log.debug("응답 송신 : [" + resRawData.length + " bytes], [" + sb + "]");
		response = Unpooled.copiedBuffer(resRawData);

		if (mode == 0) {
			ctx.writeAndFlush(response);
			log.debug("응답 프록시로 송신 Remote host: [" + ctx.channel().remoteAddress() + "]");
		} else {
			Channel channel = parent.findAgainstSession(remoteAddr);
			channel.writeAndFlush(response);
			log.debug("응답 다른 프록시로 송신 Remote host: [" + channel.remoteAddress() + "]");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
}
