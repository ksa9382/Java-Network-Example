package kr.co.direa.nettyserver.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

	@Value("${async.thread-pool.core-size}")
	private int corePoolSize;

	@Value("${async.thread-pool.max-size}")
	private int maxPoolSize;

	@Value("${async.thread-pool.queue-capacity}")
	private int queueCapacity;

	@Value("${async.thread-pool.thread-name-prefix}")
	private String threadNamePrefix;

	@Bean(name = "taskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		// application.yml에서 읽어온 설정값 적용
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix(threadNamePrefix);

		executor.initialize();
		return executor;
	}
}