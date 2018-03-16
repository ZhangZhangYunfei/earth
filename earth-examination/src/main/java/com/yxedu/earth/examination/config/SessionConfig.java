package com.yxedu.earth.examination.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableRedisHttpSession
public class SessionConfig {
  /**
   * Replaces default spring's thread executor:SimpleAsyncExecutor with our customized thread pool
   * executor.
   */
  @Bean
  public TaskExecutor springSessionRedisTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(30);
    executor.setQueueCapacity(3000);
    executor.setThreadGroupName("SpringSessionRedis-");
    executor.setThreadNamePrefix("SpringSessionRedis-");
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
    executor.initialize();
    return executor;
  }
}
