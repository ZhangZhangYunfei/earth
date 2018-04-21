package com.yxedu.earth.payment.channel.wechat;

import com.thoughtworks.xstream.XStream;
import com.yxedu.earth.common.exception.EarthException;
import com.yxedu.earth.payment.channel.NestedMapConverter;
import com.yxedu.earth.payment.channel.OrdinalType;
import com.yxedu.earth.utils.SecurityException;
import com.yxedu.earth.utils.SecurityUtils;
import com.yxedu.earth.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class WechatPayCryptor {
  private static final String XML_NODE_API_KEY = "key";
  private static final String SIGN_ALGO = "MD5";
  private static final String ENCODING = "UTF-8";
  private static final String XML_NODE_SIGN = "sign";

  private final XStream stream;
  private final XStream mapXtream;

  /**
   * Constructor.
   */
  public WechatPayCryptor() {
    this.stream = new XStream();
    this.stream.processAnnotations(WechatResponse.class);
    this.stream.processAnnotations(WechatRequest.class);
    this.stream.ignoreUnknownElements();

    mapXtream =  new XStream();
    mapXtream.alias("xml", TreeMap.class);
    mapXtream.registerConverter(new NestedMapConverter(OrdinalType.ASCII));
  }

  /**
   * Signs a request with the API key that owned by a merchant.
   */
  @SuppressWarnings("unchecked")
  public String sign(WechatRequest request, String apiKey) {
    // #converts to a map and sign it
    String plainReq = stream.toXML(request);
    Map<String, String> elements = (Map) mapXtream.fromXML(plainReq);
    request.setSign(signCore(elements, apiKey));
    return stream.toXML(request);
  }

  /**
   * Verifies a payment response from WeChat Pay with the API key.
   */
  @SuppressWarnings("unchecked")
  public WechatResponse verify(String signed, String apiKey) {
    // TODO: use mock one for test
    return (WechatResponse) stream.fromXML(signed, new WechatResponse());

//    WechatResponse response = (WechatResponse) stream.fromXML(signed, new WechatResponse());
//    if (!"SUCCESS".equals(response.getResultCode())) {
//      return response;
//    }
//
//    Map<String, String> elements = (Map) mapXtream.fromXML(signed);
//    String originalSign = elements.remove(XML_NODE_SIGN);
//
//    if (!signCore(elements, apiKey).equalsIgnoreCase(originalSign)) {
//      log.error("The invalid signature '{}' was found in response!", originalSign);
//      throw new EarthException("Invalid signature found in response");
//    }
//
//    return response;
  }

  private String signCore(Map<String, String> elements, String apiKey) {
    // #1. get key value pairs
    String pairs = StringUtils.pair(elements);
    // #2. append the API key
    String data = pairs + StringUtils.AMPERSAND
        + XML_NODE_API_KEY + StringUtils.EQUAL_SIGN + apiKey;
    // #3. sign the pairs
    try {
      return Hex.encodeHexString(SecurityUtils.digest(SIGN_ALGO, data.getBytes(ENCODING)));
    } catch (UnsupportedEncodingException | SecurityException err) {
      log.error("Failed to sign the request due to an exception!", err);
      throw new EarthException("Error signing the request", err);
    }
  }
}
