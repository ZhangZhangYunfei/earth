package com.yxedu.earth.examination.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateExaminationRequest {
  @NotNull
  private Long id;

  private String subject;

  private String requirement;

  private String description;
}
