package com.yxedu.earth.payment.channel;

import com.yxedu.earth.common.exception.EarthException;
import com.yxedu.earth.payment.channel.wechat.WechatPayProcessor;
import com.yxedu.earth.payment.channel.wechat.WechatRequestBuilder;
import com.yxedu.earth.payment.domain.PaymentType;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Factory {

  private final HttpClient client;
  private final String ipAddress;
  private final String callbackUrl;
  private final String wechatPayUrl;

  private volatile WechatPayProcessor wechatPayProcessor;

  /**
   * Constructor.
   */
  @Autowired
  public Factory(HttpClient client,
                 @Value("${IP_ADDRESS}") String ipAddress,
                 @Value("${CALLBACK_URL}") String callbackUrl,
                 @Value("${channel.wechatpay.url}") String wechatPayUrl) {
    this.client = client;
    this.ipAddress = ipAddress;
    this.callbackUrl = callbackUrl;
    this.wechatPayUrl = wechatPayUrl;
  }

  private WechatPayProcessor wechatPayProcessor() {
    if (wechatPayProcessor == null) {
      synchronized (this) {
        if (wechatPayProcessor == null) {
          wechatPayProcessor = new WechatPayProcessor(client,
              new WechatRequestBuilder(ipAddress, callbackUrl, wechatPayUrl));
        }
      }
    }
    return wechatPayProcessor;
  }

  /**
   * Get channel processor by conditions.
   */
  public Processor getProcessor(PaymentType paymentType) {
    switch (paymentType) {
      case WECHATPAY:
        return wechatPayProcessor();
      default:
        throw new EarthException("Not supported yet!");
    }
  }
}
