package com.yxedu.earth.user;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.security.Security;

@SpringBootApplication
@EnableDiscoveryClient
public class App {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
