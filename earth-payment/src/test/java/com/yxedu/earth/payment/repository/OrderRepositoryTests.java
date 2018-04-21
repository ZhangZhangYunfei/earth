package com.yxedu.earth.payment.repository;

import com.yxedu.earth.payment.domain.Order;
import com.yxedu.earth.payment.domain.OrderStatus;
import com.yxedu.earth.payment.domain.PaymentType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepositoryTests {

  @Autowired
  private OrderRepository repository;

  @Test
  public void shouldSaveAndFindOrder() {
    Order order = Order.builder()
        .amount(BigDecimal.ONE)
        //.callbackUrl("www.1.com")
        .description("Test")
        .merchantId(123L)
        .no("123")
        .paymentType(PaymentType.WECHATPAY)
        .productId("Test")
        .userId(23L)
        .status(OrderStatus.CREATED)
        .createdTime(LocalDateTime.now())
        .updatedTime(LocalDateTime.now())
        .executedTime(LocalDateTime.now())
        .code("")
        .message("")
        .build();
    Order saved = repository.save(order);

    Assert.assertNotNull(saved.getId());

    Optional<Order> found = repository.findByMerchantIdAndNo(order.getMerchantId(), order.getNo());
    Assert.assertTrue(found.isPresent());
  }
}
