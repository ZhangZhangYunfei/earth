package com.yxedu.earth.examination.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateRegistrationRequest {
  @NotNull
  private Long id;

  private String others;
}
