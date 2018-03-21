package com.yxedu.earth.examination.clients;

import com.yxedu.earth.common.UniformResponse;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("user")
public interface UserClient {
  @RequestMapping(method = RequestMethod.GET, value = "/users/hello")
  String getUsers();
}

