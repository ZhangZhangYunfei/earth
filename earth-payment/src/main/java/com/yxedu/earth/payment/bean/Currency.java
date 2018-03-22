package com.yxedu.earth.payment.bean;

public enum Currency {
  CNY("人民币"),
  USD("美元");

  private String description;

  Currency(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
