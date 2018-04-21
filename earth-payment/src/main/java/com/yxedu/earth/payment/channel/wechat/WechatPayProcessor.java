package com.yxedu.earth.payment.channel.wechat;

import com.google.common.base.Strings;

import com.yxedu.earth.common.exception.EarthException;
import com.yxedu.earth.payment.channel.Processor;
import com.yxedu.earth.payment.channel.Response;
import com.yxedu.earth.payment.domain.Order;
import com.yxedu.earth.payment.domain.OrderStatus;
import com.yxedu.earth.payment.domain.Secret;
import com.yxedu.earth.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class WechatPayProcessor implements Processor {
  private static final String API_ORDER = "/pay/unifiedorder";
  private static final String API_ORDER_QUERY = "/pay/orderquery";

  private static final String CODE_ORDER_NOT_EXIST = "ORDERNOTEXIST";
  private static final String CODE_SUCCESS = "SUCCESS";
  private static final String CODE_FAIL = "FAIL";
  private static final String CODES_FAILED = "REFUND|CLOSED|REVOKED";

  private static final Header CONTENT_TYPE = new BasicHeader("Content-Type", "text/xml");

  private final HttpClient client;
  private final WechatRequestBuilder requestBuilder;
  private final WechatPayCryptor cryptor;

  /**
   * Make checkstyle happy.
   */
  public WechatPayProcessor(HttpClient client, WechatRequestBuilder requestBuilder) {
    this.client = client;
    this.requestBuilder = requestBuilder;
    this.cryptor = new WechatPayCryptor();
  }

  @Override
  public <T extends Response> T execute(Order order, Secret secret) {
    // #1. build the request and sign it
    WechatRequest request = requestBuilder.build(order, secret);
    String signed = cryptor.sign(request, secret.getApiKey());

    // #2. executeRequest the request
    String plainResp = executeRequest(signed, request.getBaseUrl() + API_ORDER);

    // #3. verify the response
    WechatResponse wechatResponse = cryptor.verify(getMockResponse(), secret.getApiKey());

    // #4. translate the response
    return (T) translateResponse(wechatResponse, false);
  }

  @Override
  public Map<String, String> generate(Order order, Secret secret) {
    return null;
  }

  @Override
  public <T extends Response> T query(Order order, Secret secret) {
    // #1. build the request and sign it
    WechatRequest request = requestBuilder.buildQuery(order, secret);
    String signed = cryptor.sign(request, secret.getApiKey());

    // #2. executeRequest the request
    String plainResp = executeRequest(signed, request.getBaseUrl() + API_ORDER_QUERY);

    // #3. verify the response
    WechatResponse wechatResponse = cryptor.verify(getMockResponse2(), secret.getApiKey());

    // #4. translate the response
    return (T) translateResponse(wechatResponse, true);
  }

  private String executeRequest(String content, String api) {
    HttpPost requestBase = new HttpPost(api);
    requestBase.setHeader(CONTENT_TYPE);
    requestBase.setEntity(new StringEntity(content, StringUtils.UTF_8));
    HttpEntity responseEntity;
    String plainResp;
    try {
      responseEntity = client.execute(requestBase).getEntity();
      plainResp = EntityUtils.toString(responseEntity, StringUtils.UTF_8);
    } catch (IOException e) {
      log.error("Failed to executeRequest the request.", e);
      throw new EarthException("Execute the request failed.");
    }
    return plainResp;
  }

  private Response translateResponse(WechatResponse wechatResponse, boolean isQuery) {
    Response response = new Response();
    response.setPayUrl(wechatResponse.getQuickResponseCodeUrl());
    response.setCode(wechatResponse.getErrorCode() == null
        ? wechatResponse.getCode()
        : wechatResponse.getMessage());
    response.setMessage(wechatResponse.getErrorMessage() == null
        ? wechatResponse.getMessage()
        : wechatResponse.getErrorMessage());

    OrderStatus status;
    if (CODE_SUCCESS.equals(wechatResponse.getCode())
        || (!Strings.isNullOrEmpty(wechatResponse.getResultCode())
        && CODE_SUCCESS.endsWith(wechatResponse.getResultCode()))) {
      if (CODE_SUCCESS.equals(wechatResponse.getTransactionStatus())) {
        status = OrderStatus.SUCCEED;
      } else if (CODE_ORDER_NOT_EXIST.equals(wechatResponse.getErrorCode())) {
        status = OrderStatus.FAILED;
      } else if (!Strings.isNullOrEmpty(wechatResponse.getTransactionStatus())
          && CODES_FAILED.contains(wechatResponse.getTransactionStatus())) {
        status = OrderStatus.FAILED;
      } else {
        status = OrderStatus.PROCESSING;
      }
    } else {
      status = isQuery ? OrderStatus.PROCESSING : OrderStatus.FAILED;
    }
    response.setStatus(status);

    return response;
  }

  private String getMockResponse() {
    return "<xml>"
        + "<return_code><![CDATA[SUCCESS]]></return_code>\n"
        + "<return_msg><![CDATA[OK]]></return_msg>\n"
        + "<appid><![CDATA[wxcb6455908549e98c]]></appid>\n"
        + "<mch_id><![CDATA[1243560602]]></mch_id>\n"
        + "<nonce_str><![CDATA[nX6j9QeiUm6BRsrJ]]></nonce_str>\n"
        + "<sign><![CDATA[0FA08CAB09201EF4307F7B8F103AD52A]]></sign>\n"
        + "<result_code><![CDATA[SUCCESS]]></result_code>\n"
        + "<code_url><![CDATA[weixin：//wxpay/s/An4baqw]]></code_url>\n"
        + "<prepay_id><![CDATA[wx2016012020132372c9211bee0655860298]]></prepay_id>\n"
        + "<trade_type><![CDATA[APP]]></trade_type>\n"
        + "</xml>";
  }

  private String getMockResponse2() {
    return "<xml>"
        + "<return_code><![CDATA[SUCCESS]]></return_code>\n"
        + "<return_msg><![CDATA[OK]]></return_msg>\n"
        + "<appid><![CDATA[wxcb6455908549e98c]]></appid>\n"
        + "<mch_id><![CDATA[1243560602]]></mch_id>\n"
        + "<nonce_str><![CDATA[nX6j9QeiUm6BRsrJ]]></nonce_str>\n"
        + "<sign><![CDATA[0FA08CAB09201EF4307F7B8F103AD52A]]></sign>\n"
        + "<result_code><![CDATA[SUCCESS]]></result_code>\n"
        + "<code_url><![CDATA[weixin：//wxpay/s/An4baqw]]></code_url>\n"
        + "<trade_state><![CDATA[SUCCESS]]></trade_state>\n"
        + "<prepay_id><![CDATA[wx2016012020132372c9211bee0655860298]]></prepay_id>\n"
        + "<trade_type><![CDATA[APP]]></trade_type>\n"
        + "</xml>";
  }
}
