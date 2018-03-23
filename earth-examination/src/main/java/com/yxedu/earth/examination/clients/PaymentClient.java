package com.yxedu.earth.examination.clients;

import com.yxedu.earth.common.UniformResponse;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("payment")
public interface PaymentClient {
  @RequestMapping(method = RequestMethod.GET, value = "/payments")
  UniformResponse getPayments();
}
