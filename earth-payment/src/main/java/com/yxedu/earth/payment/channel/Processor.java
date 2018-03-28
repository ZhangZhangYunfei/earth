package com.yxedu.earth.payment.channel;

import com.yxedu.earth.payment.domain.Order;
import com.yxedu.earth.payment.domain.Secret;

import java.util.Map;

public interface Processor {
  /**
   * Execute order by api.
   */
  <T extends Response> T execute(Order order, Secret secret);

  /**
   * Generate pay url.
   */
  Map<String, String> generate(Order order, Secret secret);

  /**
   * Query order.
   */
  <T extends Response> T query(Order order, Secret secret);
}
