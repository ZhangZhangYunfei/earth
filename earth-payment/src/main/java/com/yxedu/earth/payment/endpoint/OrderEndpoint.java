package com.yxedu.earth.payment.endpoint;

import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.payment.bean.OrderRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RequestMapping(path = "/orders",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class OrderEndpoint {

  private static final String KEY_ID = "id";

  /**
   * Create an order.
   */
  @PostMapping
  public UniformResponse create(@RequestBody @Valid OrderRequest request) {
    return UniformResponse.success("success");
  }

  /**
   * Query an order.
   */
  @PostMapping
  public UniformResponse get(@RequestParam("merchantId") String merchantId,
                             @RequestParam("no") String no) {
    return UniformResponse.success("success");
  }
}
