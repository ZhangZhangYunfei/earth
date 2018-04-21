package com.yxedu.earth.examination.clients;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PaymentOrderRequest {
  private Long merchantId;
  private String paymentType;
  private String no;
  private Long userId;
  private BigDecimal amount;
  private String productId;
  private String description;
}
