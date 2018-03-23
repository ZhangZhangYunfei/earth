package com.yxedu.earth.examination.bean;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateExaminationRequest {
  @NotNull
  private Long merchantId;

  @NotEmpty
  private String subject;

  @NotEmpty
  private String requirement;

  @NotEmpty
  private String description;

  @NotNull
  @Digits(integer = 6, fraction = 2)
  private BigDecimal price;
}
