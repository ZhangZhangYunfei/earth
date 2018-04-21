package com.yxedu.earth.payment.service;

import com.yxedu.earth.payment.channel.Factory;
import com.yxedu.earth.payment.channel.Processor;
import com.yxedu.earth.payment.channel.Response;
import com.yxedu.earth.payment.domain.Order;
import com.yxedu.earth.payment.domain.OrderStatus;
import com.yxedu.earth.payment.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class QueryOrderAction implements OrderAction {
  private final OrderRepository orderRepository;
  private final Factory factory;

  public QueryOrderAction(OrderRepository orderRepository, Factory factory) {
    this.orderRepository = orderRepository;
    this.factory = factory;
  }


  @Override
  public OrderData call(OrderData data) {
    Order order = data.getOrder();
    log.info("Querying order:'{}' for merchant:'{}'.", order.getNo(), order.getMerchantId());
    // 1. only executed order or timeout order can be queried.
    if (order.getStatus() != OrderStatus.SENT
        && order.getStatus() != OrderStatus.PROCESSING
        && !isTimeOut(order)) {
      return data;
    }

    // 2. query order
    Processor processor = factory.getProcessor(order.getPaymentType());
    Response response = processor.query(order, data.getSecret());

    // 3. update status according to the response
    order.setStatus(response.getStatus());
    order.setCode(response.getCode());
    order.setMessage(response.getMessage());
    order.setUpdatedTime(LocalDateTime.now());
    order = orderRepository.save(order);
    //eventPublisher.publish(transfer);
    log.info("Queried order:'{}' for merchant:'{}'.", order.getNo(), order.getMerchantId());
    data.setOrder(order);
    return data;
  }

  private boolean isTimeOut(Order order) {
    return order.getStatus() == OrderStatus.CREATED
        && order.getCreatedTime().plusMinutes(30).isAfter(LocalDateTime.now());
  }
}
