package com.yxedu.earth.examination.bean;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateRegistrationRequest {
  @NotNull
  private Long examinationId;

  @NotEmpty
  private String others;
}
