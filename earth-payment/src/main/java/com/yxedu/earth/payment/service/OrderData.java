package com.yxedu.earth.payment.service;

import com.yxedu.earth.payment.domain.Order;
import com.yxedu.earth.payment.domain.Secret;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class OrderData {
  private Order order;
  private Secret secret;
}
