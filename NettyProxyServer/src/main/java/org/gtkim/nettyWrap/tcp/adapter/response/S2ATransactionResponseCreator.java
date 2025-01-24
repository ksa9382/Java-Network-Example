package org.gtkim.nettyWrap.tcp.adapter.response;

public class S2ATransactionResponseCreator implements ResponseCreator {
	@Override
	public String makeResponse(String request) {
		return request.replace("S2AQ", "S2AR");
	}
}
