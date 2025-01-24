package org.gtkim;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable // Indicate that a class or method is safe to be used concurrently by multiple channels.
// This means that the state of the class or the method can be shared among multiple instances without the risk of race conditions or data corruption. By marking a class or a method with this annotation, you are indicating that it is thread-safe and can be reused across multiple channels, which can lead to improved performance and reduced memory usage.
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	Logger logger = LogManager.getLogger(EchoServerHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("Client [" + ctx.channel().remoteAddress() + "] is connected.");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("Client [" + ctx.channel().remoteAddress() + "] is disconnected.");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf)msg;

		byte[] rawData = new byte[in.readableBytes()];
		in.readBytes(rawData);

		String request = new String(rawData);

		logger.debug("Request: {}", request);
		// String response = request.replace("A2SQ", "A2SR");
		// String response = request;
		String response = request.replace("REQUESTS", "RESPONSE");

		// Thread.sleep(5000);

		logger.debug("Response: {}", response);
		ByteBuf responseBuf = Unpooled.copiedBuffer(response.getBytes(CharsetUtil.UTF_8));

		// ctx.write(responseBuf); // Not flush yet.
		Channel channel = ctx.channel();
		channel.writeAndFlush(responseBuf);

		// String remoteAddr = ctx.channel().remoteAddress().toString();
		//
		// ByteBuf buf = Unpooled.copiedBuffer((ByteBuf)msg);
		// byte[] rawData = new byte[buf.readableBytes()];
		// buf.readBytes(rawData);
		// String data = new String(rawData);
		//
		// logger.debug("Received data[" + data + "] from remote host[" + remoteAddr + "].");
		//
		// int index = 0;
		// String length = data.substring(index, index + 4);
		// index += 4;
		// logger.debug("length = " + length);
		//
		// String tranCode = data.substring(index, index + 9);
		// index += 9;
		// logger.debug("tranCode = " + tranCode);
		//
		// String chnIntfId = data.substring(index, index + 11);
		// index += 11;
		// logger.debug("chnIntfId = " + chnIntfId);
		//
		// String msgNo = data.substring(index, index + 4);
		// index += 4;
		// logger.debug("msgNo = " + msgNo);
		//
		// String msgCode = data.substring(index, index + 6);
		// index += 6;
		// logger.debug("msgCode = " + msgCode);
		//
		// String filler = data.substring(index, index + 9);
		// index += 9;
		// logger.debug("filler = " + filler);
		//
		// String tranUnqNo = data.substring(index, index + 12);
		// index += 12;
		// logger.debug("tranUnqNo = " + tranUnqNo);
		//
		// String filler2 = data.substring(index, index += 9);
		// logger.debug("filler2 = " + filler2);
		//
		// // TODO: 전문처리
		// ByteBuf response;
		// logger.debug("요청 수신");
		// length = "0060";
		// String dummy = chnIntfId.substring(0, 6);
		// msgNo = "0210";
		// filler = "99999999999999999999999";
		// filler2 = "999999999";
		//
		// StringBuilder sb = new StringBuilder();
		// sb.append(length).append(dummy).append(msgNo).append(msgCode).append(filler).append(tranUnqNo).append(filler2);
		//
		// byte[] resRawData = sb.toString().getBytes();
		//
		// logger.debug("응답 송신 : [" + resRawData.length + " bytes], [" + sb + "]");
		// response = Unpooled.copiedBuffer(resRawData);
		//
		// ctx.writeAndFlush(response);
		// logger.debug("응답 프록시로 송신 Remote host: [{}], Msg: [{}]", ctx.channel().remoteAddress(), sb.toString());
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// Flush waiting messages on the channel and close one.
		// ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
		// 	.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getMessage());
		ctx.close();
	}
}
