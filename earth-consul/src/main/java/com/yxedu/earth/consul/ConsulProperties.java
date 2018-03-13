package com.yxedu.earth.consul;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("consul")
@Data
public class ConsulProperties {

  private String prop = "default value";
}
