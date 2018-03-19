package com.yxedu.earth.user.bean;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
public class RegisterUserRequest {

  @NotEmpty
  private String idNo;

  @NotEmpty
  private String telephone;

  @NotEmpty
  private String username;

  @NotEmpty
  private String password;

  @NotEmpty
  private String captcha;
}
