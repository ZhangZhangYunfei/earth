package com.yxedu.earth.examination.endpoint;

import com.yxedu.earth.examination.domain.Merchant;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(path = "/merchants",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class MerchantEndpoint {

  @RequestMapping("/")
  public Merchant index() {
    return new Merchant();
  }

}
