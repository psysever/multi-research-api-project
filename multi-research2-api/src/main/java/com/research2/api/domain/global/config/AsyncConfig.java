package com.research2.api.domain.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // 기본 실행될 스레드 개수
        executor.setMaxPoolSize(10);  // 최대 생성할 수 있는 스레드 개수
        executor.setQueueCapacity(50); // 작업이 대기할 수 있는 큐 크기
        executor.setThreadNamePrefix("AsyncExecutor-"); // 생성되는 스레드 이름
        executor.initialize();
        return executor;
    }
}
