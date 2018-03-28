package com.yxedu.earth.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OrderStatus {
  CREATED("已创建"),
  SENT("已发送"),
  PROCESSING("处理中"),
  SUCCEED("成功"),
  FAILED("失败");

  private String description;
}
