package org.gtkim;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.gtkim.example.nettyWrap.tcp.adapter.AsyncTcpServer;
import org.gtkim.nettyWrap.tcp.adapter.handler.LoadBalanceHandler;

import io.netty.channel.Channel;

public class LoadBalanceServer extends AsyncTcpServer {
	private static final ConcurrentHashMap<String, Channel> sessionMap = new ConcurrentHashMap<String, Channel>();

	public LoadBalanceServer(int port, LoadBalanceHandler handler) {
		super(port, handler);
		handler.bindParent(this);
	}

	public boolean isEnrolled(String address) {
		return sessionMap.get(address) != null;
	}

	public void enrollSession(String address, Channel channel) {
		sessionMap.put(address, channel);
	}

	public Channel findSession(String address) {
		Channel channel = sessionMap.get(address);
		return channel;
	}

	public Channel findAgainstSession(String address) {
		Channel channel = null;
		Set<Map.Entry<String, Channel>> entrySet = sessionMap.entrySet();

		for (Map.Entry<String, Channel> entry : entrySet) {
			Channel tempChannel = entry.getValue();
			if (address.equals(tempChannel.remoteAddress().toString()))
				continue;
			else
				return tempChannel;
		}

		return channel;
	}

	public void deleteSession(String address) {
		try {
			sessionMap.remove(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
