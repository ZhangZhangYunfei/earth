package com.yxedu.earth.consul;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@SpringBootApplication
//@EnableDiscoveryClient
//@EnableConsulUi
//@RestController
@EnableConfigurationProperties
@Slf4j
public class App {
//public class App implements ApplicationListener<SimpleRemoteEvent> {

//  @Autowired
//  private LoadBalancerClient loadBalancer;
//
//  @Autowired
//  private DiscoveryClient discoveryClient;

  @Autowired
  private Environment env;

//  @Autowired(required = false)
//  private RelaxedPropertyResolver resolver;

  @Value("${spring.application.name:consul}")
  private String appName;

  /**
   * make check style happy.
   */
//  @PostConstruct
//  public void init() {
//    if (resolver == null) {
//      resolver = new RelaxedPropertyResolver(env);
//    }
//  }

//  @Bean
//  public SubtypeModule sampleSubtypeModule() {
//    return new SubtypeModule(SimpleRemoteEvent.class);
//  }

  @Bean
  public ConsulProperties sampleProperties() {
    return new ConsulProperties();
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

//  @Override
//  public void onApplicationEvent(SimpleRemoteEvent event) {
//    log.info("Received event: {}", event);
//  }
}
