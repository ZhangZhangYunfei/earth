package com.yxedu.earth.payment.bean;

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
public class OrderRequest {
  @NotEmpty
  private String merchantId;

  @NotNull
  private PaymentType paymentType;

  @NotEmpty
  @Length(max = 32)
  private String no;

  private String returnUrl;

  private String callbackUrl;

  @NotNull
  @DecimalMin("0.01")
  @Digits(integer = 6, fraction = 2)
  private BigDecimal amount;

  private Currency currency = Currency.CNY;
}
