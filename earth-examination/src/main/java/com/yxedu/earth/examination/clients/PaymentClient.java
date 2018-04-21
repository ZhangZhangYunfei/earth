package com.yxedu.earth.examination.clients;

import com.yxedu.earth.common.UniformResponse;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "payment", configuration = ClientConfiguration.class)
public interface PaymentClient {
  @RequestMapping(method = RequestMethod.POST, value = "/orders")
  UniformResponse getPayUrl(@RequestBody PaymentOrderRequest request);

  @RequestMapping(method = RequestMethod.GET, value = "/orders")
  UniformResponse getOrder(@RequestParam("merchantId") Long merchantId,
                           @RequestParam("no") String no);
}
