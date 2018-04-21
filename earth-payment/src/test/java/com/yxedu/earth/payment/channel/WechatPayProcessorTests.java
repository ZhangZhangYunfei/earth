package com.yxedu.earth.payment.channel;


import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.yxedu.earth.payment.channel.wechat.WechatPayProcessor;
import com.yxedu.earth.payment.channel.wechat.WechatRequestBuilder;
import com.yxedu.earth.payment.domain.Order;
import com.yxedu.earth.payment.domain.OrderStatus;
import com.yxedu.earth.payment.domain.PaymentType;
import com.yxedu.earth.payment.domain.Secret;

import org.apache.http.client.HttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatPayProcessorTests {

  private WechatPayProcessor processor;

  @Autowired
  private HttpClient httpClient;

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(8089);

  @Before
  public void setUp() {
    processor = new WechatPayProcessor(httpClient,
        new WechatRequestBuilder("127.0.0.1",
            "www.1.com", "http://localhost:8089"));
  }

  @Test
  public void submit() {
    wireMockRule.stubFor(post(urlPathEqualTo("/pay/unifiedorder"))
        .willReturn(aResponse()
            .withBody(getResp())
            .withHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
            .withStatus(200)));

    Order order = getOrder();
    Secret secret = getSecret();
    Response response = processor.execute(order, secret);

    Assert.assertEquals(response.getCode(), "SUCCESS");
  }


  @Test
  public void query() {
    wireMockRule.stubFor(post(urlPathEqualTo("/pay/orderquery"))
        .willReturn(aResponse()
            .withBody(getResp())
            .withHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
            .withStatus(200)));

    Order order = getOrder();
    Secret secret = getSecret();
    Response response = processor.query(order, secret);

    Assert.assertEquals(response.getCode(), "SUCCESS");
  }

  private Order getOrder() {
    return Order.builder()
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
  }

  private Secret getSecret() {
    Secret secret = new Secret();
    secret.setAppId("1234");
    secret.setApiKey("81OBE4Mo0g6NhShYDPpUYVBg7GpB9bH9");
    secret.setMerchantNo("1243560602");
    return secret;
  }

  private String getResp() {
    return "<xml>" +
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
  }
}
