package com.yxedu.earth.payment.bean;

import com.yxedu.earth.payment.domain.Currency;
import com.yxedu.earth.payment.domain.PaymentType;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateOrderRequest {
  @NotNull
  private Long merchantId;

  @NotNull
  private PaymentType paymentType;

  @NotEmpty
  @Length(max = 32)
  private String no;

  @NotNull
  private Long userId;

  @NotNull
  @DecimalMin("0.01")
  @Digits(integer = 6, fraction = 2)
  private BigDecimal amount;

  @NotEmpty
  private String productId;

  @NotEmpty
  private String description;

  private String returnUrl;

  private String callbackUrl;

  private Currency currency = Currency.CNY;
}
