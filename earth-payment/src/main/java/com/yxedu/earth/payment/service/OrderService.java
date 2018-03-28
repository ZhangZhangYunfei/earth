package com.yxedu.earth.payment.service;

import com.yxedu.earth.common.exception.EarthException;
import com.yxedu.earth.payment.bean.CreateOrderRequest;
import com.yxedu.earth.payment.domain.Order;
import com.yxedu.earth.payment.domain.Secret;
import com.yxedu.earth.payment.repository.OrderRepository;
import com.yxedu.earth.payment.repository.SecretRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class OrderService {
  private final CreateOrderAction createOrderAction;
  private final SendOrderAction sendOrderAction;
  private final QueryOrderAction queryOrderAction;
  private final OrderRepository orderRepository;
  private final SecretRepository secretRepository;

  /**
   * Constructor.
   */
  public OrderService(CreateOrderAction createOrderAction,
                      SendOrderAction sendOrderAction,
                      QueryOrderAction queryOrderAction,
                      OrderRepository orderRepository,
                      SecretRepository secretRepository) {
    this.createOrderAction = createOrderAction;
    this.sendOrderAction = sendOrderAction;
    this.queryOrderAction = queryOrderAction;
    this.orderRepository = orderRepository;
    this.secretRepository = secretRepository;
  }

  /**
   * Create order.
   */
  public OrderData genPayUrl(CreateOrderRequest request) {
    Order order = Order.builder()
        .amount(request.getAmount())
        //.callbackUrl(request.getCallbackUrl())
        .description(request.getDescription())
        .merchantId(request.getMerchantId())
        .no(request.getNo())
        .paymentType(request.getPaymentType())
        .productId(request.getProductId())
        .userId(request.getUserId())
        .build();
    Secret secret = secretRepository.findByMerchantIdAndType(request.getMerchantId(),
        request.getPaymentType())
        .orElseThrow(() -> new EarthException("The secret is not found."));

    OrderData data = OrderData.builder()
        .order(order)
        .secret(secret)
        .build();

    data = createOrderAction.call(data);
    data = sendOrderAction.call(data);

    return data;
  }

  /**
   * Query order.
   */
  public Order queryOrder(Long merchantId, String no) {
    Optional<Order> order = orderRepository.findByMerchantIdAndNo(merchantId, no);
    if (!order.isPresent()) {
      log.error("The order is not existed");
      throw new EarthException("Order not existed.");
    }
    Secret secret = secretRepository.findByMerchantIdAndType(order.get().getMerchantId(),
        order.get().getPaymentType())
        .orElseThrow(() -> new EarthException("The secret is not found."));

    OrderData data = OrderData.builder()
        .order(order.get())
        .secret(secret)
        .build();

    return queryOrderAction.call(data).getOrder();
  }
}
