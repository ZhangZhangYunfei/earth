package com.yxedu.earth.examination.bean;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
public class CreateRegistrationRequest {
  @NotEmpty
  private Long examinationId;

  @NotEmpty
  private String others;
}
