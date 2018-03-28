package com.yxedu.earth.payment.bean;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateSecretRequest {
  @NotNull
  private Long id;

  private String merchantNo;

  private String appId;

  private String apiKey;
}
