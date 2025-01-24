package kr.co.direa.nettyserver.processor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AsyncProcessorImpl implements AsyncProcessor {
	@Value("${tgrm.replace-offset}")
	private int replaceOffset;
	@Value("${tgrm.replace-string}")
	private String replaceString;

	@Async("taskExecutor") // 직접 정의한 Executor를 명시적으로 사용
	public void process(String request, ChannelHandlerContext ctx) {
		log.debug("Received message: {}", request);

		String response = request.substring(0, replaceOffset) + replaceString + request.substring(
			replaceOffset + replaceString.length());

		log.debug("Response message: {}", response);

		// 클라이언트로 응답 전송
		ctx.writeAndFlush(response);
	}
}