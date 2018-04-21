package com.yxedu.earth.user.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AssignEmployRequest {
  @NotNull
  Long merchantId;

  @NotNull
  Long employeeId;
}
