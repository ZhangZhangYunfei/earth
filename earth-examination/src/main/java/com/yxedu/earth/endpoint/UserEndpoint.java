package com.yxedu.earth.endpoint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserEndpoint {

  @RequestMapping("/")
  public String index() {
    return "Greetings from Spring Boot!";
  }

}
