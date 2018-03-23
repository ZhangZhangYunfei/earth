package com.yxedu.earth.examination.clients;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "user", configuration = ClientConfiguration.class)
public interface UserClient {
  @RequestMapping(method = RequestMethod.GET, value = "/users/hello/a")
  String getUsers();
}

