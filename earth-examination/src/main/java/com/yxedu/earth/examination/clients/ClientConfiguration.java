package com.yxedu.earth.examination.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
  @Bean
  public SessionAuthRequestInterceptor sessionAuthRequestInterceptor() {
    return new SessionAuthRequestInterceptor();
  }
}
