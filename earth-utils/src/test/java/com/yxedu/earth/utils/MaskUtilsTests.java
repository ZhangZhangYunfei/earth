package com.yxedu.earth.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class MaskUtilsTests {

  public static final String BANK_ACCOUNT_NO = "bankAccountNo";

  public void maskPhone1() {
    String phone = "555";
    Assert.assertEquals(MaskUtils.maskMobile(phone), phone);
  }

  public void maskPhone2() {
    String phone = "13333333333";
    Assert.assertEquals(MaskUtils.maskMobile(phone), "133****3333");
  }

  public void maskIdNo1() {
    String idNo = "555";
    Assert.assertEquals(MaskUtils.maskIdNo(idNo), idNo);
  }

  public void maskIdNo2() {
    String phone = "3101031980070709999";
    Assert.assertEquals(MaskUtils.maskIdNo(phone), "310*******9999");
  }

  public void maskCardNo1() {
    String cardNo = "888888";
    Assert.assertEquals(MaskUtils.maskCardNum(cardNo), cardNo);
  }

  public void maskCardNo2() {
    String cardNo = "6666000000009999";
    Assert.assertEquals(MaskUtils.maskCardNum(cardNo), "6666 **** **** 9999");
  }

  public void maskCardNo3() {
    String cardNo = "98000021504";
    Assert.assertEquals(MaskUtils.maskCardNum(cardNo), "9800***1504");
  }

  public void maskCardNo4() {
    String str = "PRIVATE, bankAccountNo=6666555577779999, reques";
    Assert.assertEquals(MaskUtils.maskCardNum4Str(str), "PRIVATE, bankAccountNo=6666 **** **** 9999, reques");
  }

  public void maskCardNo5() {
    String str = "PRIVATE, bankAccountNo=6666555577779999)";
    Assert.assertEquals(MaskUtils.maskCardNum4Str(str), "PRIVATE, bankAccountNo=6666 **** **** 9999)");
  }

  public void maskName1() {
    String name = "Jack";
    Assert.assertEquals(MaskUtils.maskName(name), "J***");
  }

  public void maskName2() {
    String name = "李世民";
    Assert.assertEquals(MaskUtils.maskName(name), "李**");
  }

}
