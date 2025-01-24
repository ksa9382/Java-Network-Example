package kr.co.direa.nettyserver.processor;

import io.netty.channel.ChannelHandlerContext;

public interface AsyncProcessor {
	void process(String msg, ChannelHandlerContext ctx);
}