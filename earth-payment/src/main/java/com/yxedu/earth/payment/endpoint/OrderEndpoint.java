package com.yxedu.earth.payment.endpoint;

import com.google.common.base.Strings;

import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.common.security.AuthenticationHelper;
import com.yxedu.earth.payment.bean.CreateOrderRequest;
import com.yxedu.earth.payment.domain.Order;
import com.yxedu.earth.payment.service.OrderData;
import com.yxedu.earth.payment.service.OrderService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;


@Slf4j
@RequestMapping(path = "/orders",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class OrderEndpoint {

  private static final String KEY_URL = "url";

  private final OrderService orderService;

  public OrderEndpoint(OrderService orderService) {
    this.orderService = orderService;
  }


  /**
   * Create an order and get an pay url.
   */
  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public UniformResponse create(@RequestBody @Valid CreateOrderRequest request) {
    log.info("The user {} of merchant {} is creating order for user {}.",
        AuthenticationHelper.getId(), request.getMerchantId(), request.getUserId());
    AuthenticationHelper.checkExamineeIntegrity(request.getUserId(), request.getMerchantId());

    OrderData data = orderService.genPayUrl(request);

    Map<String, String> resultMap = new HashMap<>(1);
    resultMap.put(KEY_URL, data.getOrder().getPayUrl());

    log.info("The user {} of merchant {} created order for user {}.",
        AuthenticationHelper.getId(), request.getMerchantId(), request.getUserId());
    return Strings.isNullOrEmpty(data.getOrder().getPayUrl())
        ? UniformResponse.failed(data.getOrder().getCode(), data.getOrder().getMessage())
        : UniformResponse.success(resultMap);
  }

  /**
   * Query an order.
   */
  //@PreAuthorize("isAuthenticated()")
  @GetMapping
  public UniformResponse get(@RequestParam("merchantId") Long merchantId,
                             @RequestParam("no") String no) {
    Order order = orderService.queryOrder(merchantId, no);
    log.info("The user {} of merchant {} is querying order for user {}.",
        AuthenticationHelper.getId(), merchantId, order.getUserId());
    AuthenticationHelper.checkExamineeIntegrity(order.getUserId(), merchantId);

    log.info("The user {} of merchant {} created order for user {}.",
        AuthenticationHelper.getId(), merchantId, order.getUserId());
    return UniformResponse.success(order);
  }
}
