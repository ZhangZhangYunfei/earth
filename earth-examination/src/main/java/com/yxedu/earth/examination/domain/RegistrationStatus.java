package com.yxedu.earth.examination.domain;

import lombok.Getter;

@Getter
public enum RegistrationStatus {
  CREATED("创建，初始状态"),
  PAYING("付款中"),
  PAID("已付款"),
  CONFIRMED("已确认，已被管理员确认！");

  private String description;

  RegistrationStatus(String description) {
    this.description = description;
  }
}
