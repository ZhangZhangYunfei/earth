package com.yxedu.earth.payment.channel;

import static org.mockito.MockitoAnnotations.initMocks;

import com.yxedu.earth.payment.channel.wechat.WechatPayCryptor;
import com.yxedu.earth.payment.channel.wechat.WechatRequestBuilder;
import com.yxedu.earth.payment.channel.wechat.WechatResponse;
import com.yxedu.earth.payment.domain.Order;
import com.yxedu.earth.payment.domain.OrderStatus;
import com.yxedu.earth.payment.domain.PaymentType;
import com.yxedu.earth.payment.domain.Secret;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.security.Security;
import java.time.LocalDateTime;


public class WechatPayCryptorTests {

  @InjectMocks
  private WechatPayCryptor cryptor;

  private WechatRequestBuilder requestBuilder;

  @Before
  public void setUp() {
    initMocks(this);
    Security.addProvider(new BouncyCastleProvider());
    requestBuilder = new WechatRequestBuilder("127.0.0.1",
        "www.1.com", "https://10.18.19.127:19301");
  }

  @Test
  public void signAndVerify() {
    Order order = Order.builder()
        .amount(BigDecimal.ONE)
        //.callbackUrl("www.1.com")
        .description("Test")
        .merchantId(123L)
        .no("123")
        .paymentType(PaymentType.WECHATPAY)
        .productId("Test")
        .userId(23L)
        .status(OrderStatus.CREATED)
        .createdTime(LocalDateTime.now())
        .updatedTime(LocalDateTime.now())
        .executedTime(LocalDateTime.now())
        .code("")
        .message("")
        .build();
    Secret secret = new Secret();
    secret.setAppId("1234");
    secret.setApiKey("81OBE4Mo0g6NhShYDPpUYVBg7GpB9bH9");
    secret.setMerchantNo("1243560602");

    String signed = cryptor.sign(requestBuilder.build(order, secret), secret.getApiKey());
    WechatResponse response = cryptor.verify(signed, secret.getApiKey());

    Assert.assertNotNull(response);
  }

  @Test
  public void verifyResponse() {
    String plainResponse =
        "<xml>" +
            "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "<return_msg><![CDATA[OK]]></return_msg>\n" +
            "<appid><![CDATA[wxcb6455908549e98c]]></appid>\n" +
            "<mch_id><![CDATA[1243560602]]></mch_id>\n" +
            "<nonce_str><![CDATA[nX6j9QeiUm6BRsrJ]]></nonce_str>\n" +
            "<sign><![CDATA[0FA08CAB09201EF4307F7B8F103AD52A]]></sign>\n" +
            "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
            "<prepay_id><![CDATA[wx2016012020132372c9211bee0655860298]]></prepay_id>\n" +
            "<trade_type><![CDATA[APP]]></trade_type>\n" +
            "</xml>";

    WechatResponse response = cryptor.verify(plainResponse, "81OBE4Mo0g6NhShYDPpUYVBg7GpB9bH9");
    Assert.assertNotNull(response);
  }
}
