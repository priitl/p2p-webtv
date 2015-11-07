package com.priitlaht.maurus.common.config.async;

import com.priitlaht.maurus.common.ApplicationProperties;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer {

  @Inject
  private ApplicationProperties applicationProperties;

  @Override
  @Bean(name = "taskExecutor")
  public Executor getAsyncExecutor() {
    log.debug("Creating Async Task Executor");
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(applicationProperties.getAsync().getCorePoolSize());
    executor.setMaxPoolSize(applicationProperties.getAsync().getMaxPoolSize());
    executor.setQueueCapacity(applicationProperties.getAsync().getQueueCapacity());
    executor.setThreadNamePrefix("maurus-Executor-");
    return ExceptionHandlingAsyncTaskExecutor.of(executor);
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }
}
