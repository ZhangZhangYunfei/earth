package com.yxedu.earth.payment.channel;

import com.yxedu.earth.payment.domain.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
  private OrderStatus status;
  private String code;
  private String message;
  private String payUrl;
}
