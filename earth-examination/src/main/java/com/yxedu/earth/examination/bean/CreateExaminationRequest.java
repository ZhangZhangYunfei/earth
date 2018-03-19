package com.yxedu.earth.examination.bean;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotEmpty;

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
}
