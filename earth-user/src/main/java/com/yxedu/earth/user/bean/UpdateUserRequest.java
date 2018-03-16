package com.yxedu.earth.user.bean;

import lombok.Getter;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Getter
public class UpdateUserRequest {
  @NotEmpty
  private String idNo;

  private String telephone;

  private String username;

  private String password;

  boolean enabled = true;

  boolean accountNonExpired = true;

  boolean credentialsNonExpired = true;

  boolean accountNonLocked = true;

  private List<String> authorities;
}
