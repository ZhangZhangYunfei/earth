package com.yxedu.earth.utils;

import lombok.extern.slf4j.Slf4j;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

@Test
@Slf4j
public class AmountUtilsTests {
  public void toDecimalNormCase() {
    toDecimalTestCore("8964", 89.64);
  }

  public void toDecimalZeroCase() {
    toDecimalTestCore("", 0.0);
  }

  private void toDecimalTestCore(String amountString, double expectedValue) {
    BigDecimal amountResult = AmountUtils.toDecimal(amountString);
    Assert.assertNotNull(amountResult);
    Assert.assertEquals(amountResult.doubleValue(), expectedValue);

  }

  @Test(expectedExceptions = NumberFormatException.class)
  public void invalidToDecimal() {
    AmountUtils.toDecimal("invalid");
  }

  public void withoutDecimalPoint() {
    BigDecimal from = new BigDecimal(89.64);
    BigDecimal to = AmountUtils.withoutDecimalPoint(from);

    Assert.assertNotNull(to);
    Assert.assertNotEquals(from, to);
    Assert.assertEquals(to.doubleValue(), 8964D);
  }
}
