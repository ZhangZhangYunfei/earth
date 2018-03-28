package com.yxedu.earth.payment.service;

import com.yxedu.earth.payment.domain.Order;
import com.yxedu.earth.payment.domain.OrderStatus;
import com.yxedu.earth.payment.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
public class CreateOrderAction implements OrderAction {

  @Autowired
  private OrderRepository orderRepository;

  @Override
  public OrderData call(OrderData data) {
    Order order = data.getOrder();
    log.info("Creating order:'{}' for merchant:'{}'.", order.getNo(), order.getMerchantId());
    Optional<Order> previous = orderRepository.findByMerchantIdAndNo(order.getMerchantId(),
        order.getNo());
    // 1. idepotent operation
    if (previous.isPresent()) {
      data.setOrder(previous.get());
      return data;
    }

    // 2. create new order
    order.setStatus(OrderStatus.CREATED);
    LocalDateTime now = LocalDateTime.now();
    order.setCreatedTime(now);
    order.setExecutedTime(now);
    order.setUpdatedTime(now);
    order = orderRepository.save(order);
    //eventPublisher.publish(transfer);
    log.info("Created order:'{}' for merchant:'{}'.", order.getNo(), order.getMerchantId());
    data.setOrder(order);
    return data;
  }
}
