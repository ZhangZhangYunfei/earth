package com.yxedu.earth.payment.endpoint;

import com.yxedu.earth.common.UniformResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RequestMapping(path = "/payments",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class PaymentEndpoint {

  private static final String KEY_ID = "id";

  /**
   * Update a merchant.
   */
  @GetMapping
  public UniformResponse get() {
    return UniformResponse.success("success");
  }
}
