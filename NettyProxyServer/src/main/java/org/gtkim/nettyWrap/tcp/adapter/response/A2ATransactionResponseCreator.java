package org.gtkim.nettyWrap.tcp.adapter.response;

public class A2ATransactionResponseCreator implements ResponseCreator {
	@Override
	public String makeResponse(String request) {
		return request.replace("A2AQ", "A2AR");
	}
}
