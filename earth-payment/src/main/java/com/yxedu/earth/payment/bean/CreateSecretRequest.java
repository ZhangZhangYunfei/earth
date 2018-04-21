package com.yxedu.earth.payment.bean;

import com.yxedu.earth.payment.domain.PaymentType;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateSecretRequest {
  @NotNull
  private Long merchantId;

  @NotNull
  private PaymentType type;

  @NotEmpty
  private String merchantNo;

  @NotEmpty
  private String appId;

  @NotEmpty
  private String apiKey;
}
