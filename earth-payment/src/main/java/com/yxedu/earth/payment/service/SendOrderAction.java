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
public class SendOrderAction implements OrderAction {
  private final OrderRepository orderRepository;
  private final Factory processorFactory;

  public SendOrderAction(OrderRepository orderRepository,
                         Factory processorFactory) {
    this.orderRepository = orderRepository;
    this.processorFactory = processorFactory;
  }

  @Override
  public OrderData call(OrderData data) {
    Order order = data.getOrder();
    log.info("Sending order:'{}' for merchant:'{}'.", order.getNo(), order.getMerchantId());
    // 1. only initial status can be sent in case of executing order multiple times.
    if (order.getStatus() != OrderStatus.CREATED) {
      return data;
    }

    // 2. set the order to sent
    order.setStatus(OrderStatus.SENT);
    order.setUpdatedTime(LocalDateTime.now());
    order = orderRepository.save(order);

    // 3. execute order
    Processor processor = processorFactory.getProcessor(order.getPaymentType());
    Response response = processor.execute(order, data.getSecret());

    // 4. update the status after execution of order
    order.setStatus(response.getStatus());
    order.setCode(response.getCode());
    order.setMessage(response.getMessage());
    order.setUpdatedTime(LocalDateTime.now());
    order.setExecutedTime(LocalDateTime.now());
    order.setPayUrl(response.getPayUrl());
    order = orderRepository.save(order);

    //eventPublisher.publish(transfer);
    log.info("Sent order:'{}' for merchant:'{}'.", order.getNo(), order.getMerchantId());
    data.setOrder(order);
    return data;
  }
}
