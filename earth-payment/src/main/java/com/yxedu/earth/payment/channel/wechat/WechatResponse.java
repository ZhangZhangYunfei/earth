package com.yxedu.earth.payment.channel.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XStreamAlias("xml")
public class WechatResponse {
  @XStreamAlias("return_code")
  private String code;

  @XStreamAlias("return_msg")
  private String message;

  // only if the code is SUCCESS
  @XStreamAlias("appid")
  private String appId;

  @XStreamAlias("mch_id")
  private String merchantNo;

  @XStreamAlias("device_info")
  private String deviceInfo;

  @XStreamAlias("nonce_str")
  private String nonceString;

  @XStreamAlias("sign")
  private String signature;

  @XStreamAlias("out_trade_no")
  private String orderNo;

  @XStreamAlias("attach")
  private String attach;

  @XStreamAlias("result_code")
  private String resultCode;

  @XStreamAlias("trade_state")
  private String transactionStatus;

  @XStreamAlias("err_code")
  private String errorCode;

  @XStreamAlias("err_code_des")
  private String errorMessage;

  // only if the return code & result code are SUCCESS
  @XStreamAlias("trade_type")
  private String transactionSource;

  @XStreamAlias("prepay_id")
  private String paymentTransactionId;

  @XStreamAlias("code_url")
  private String quickResponseCodeUrl;

  @XStreamAlias("time_end")
  private String finishedTime;

  @XStreamAlias("trade_state_desc")
  private String description;
}
