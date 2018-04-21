package com.yxedu.earth.payment.channel.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@XStreamAlias("xml")
public class WechatRequest {

  /**
   * 微信支付分配的公众账号ID（企业号corpid即为此appId）.
   */
  @XStreamAlias("appid")
  private String appid;

  /**
   * 微信支付分配的商户号.
   */
  @XStreamAlias("mch_id")
  private String merchantNo;

  /**
   * 随机字符串，长度要求在32位以内.
   */
  @XStreamAlias("nonce_str")
  private String randomString;

  /**
   * 通过签名算法计算得出的签名值.
   */
  @XStreamAlias("sign")
  private String sign;

  /**
   * 商品简单描述.
   */
  @XStreamAlias("body")
  private String description;

  /**
   * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母，且在同一个商户号下唯一.
   */
  @XStreamAlias("out_trade_no")
  private String no;

  /**
   * 订单总金额，单位为分.
   */
  @XStreamAlias("total_fee")
  private int amount;

  /**
   * APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP.
   */
  @XStreamAlias("spbill_create_ip")
  private String ipAddress;

  /**
   * 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数.
   */
  @XStreamAlias("notify_url")
  private String callbackUrl;

  /**
   * NATIVE 扫码支付.
   */
  @XStreamAlias("trade_type")
  private String tradeType;

  /**
   * 此参数为二维码中包含的商品ID，商户自行定义.
   */
  @XStreamAlias("product_id")
  private String productId;


  @XStreamOmitField
  private String baseUrl;
}
