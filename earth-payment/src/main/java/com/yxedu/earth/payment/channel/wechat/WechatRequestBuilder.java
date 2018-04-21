package com.yxedu.earth.payment.channel.wechat;

import com.yxedu.earth.payment.domain.Order;
import com.yxedu.earth.payment.domain.Secret;

import org.apache.commons.lang3.RandomStringUtils;

public class WechatRequestBuilder {
  private static final int MAX_SIZE = 32;
  private final String ipAddress;
  private final String callbackUrl;
  private final String baseUrl;

  /**
   * Constructor.
   */
  public WechatRequestBuilder(String ipAddress, String callbackUrl, String baseUrl) {
    this.ipAddress = ipAddress;
    this.callbackUrl = callbackUrl;
    this.baseUrl = baseUrl;
  }

  /**
   * Build request of getting pay url.
   */
  public WechatRequest build(Order order, Secret secret) {
    return WechatRequest.builder()
        .appid(secret.getAppId())
        .merchantNo(secret.getMerchantNo())
        .randomString(RandomStringUtils.randomAlphanumeric(MAX_SIZE))
        .description(order.getDescription())
        .no(order.getNo())
        .amount(Integer.valueOf(order.getAmount().movePointRight(2).toString()))
        .ipAddress(ipAddress)
        .callbackUrl(callbackUrl) // 配置
        .baseUrl(baseUrl)
        .tradeType("NATIVE")
        .productId(order.getProductId())
        .build();
  }

  /**
   * Build query order request.
   */
  public WechatRequest buildQuery(Order order, Secret secret) {
    return WechatRequest.builder()
        .baseUrl(baseUrl)
        .appid(secret.getAppId())
        .merchantNo(secret.getMerchantNo())
        .randomString(RandomStringUtils.randomAlphanumeric(MAX_SIZE))
        .no(order.getNo())
        .build();
  }
}
