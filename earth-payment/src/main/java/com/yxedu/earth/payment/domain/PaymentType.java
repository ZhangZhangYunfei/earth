package com.yxedu.earth.payment.domain;

import lombok.Getter;

@Getter
public enum PaymentType {
  WECHATPAY("微信支付"),
  ALIPAY("支付宝");

  private String fullName;

  PaymentType(String fullName) {
    this.fullName = fullName;
  }
}
