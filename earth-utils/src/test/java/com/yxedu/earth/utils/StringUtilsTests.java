package com.yxedu.earth.utils;

import lombok.extern.slf4j.Slf4j;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Test
@Slf4j
public class StringUtilsTests {
  public void pair() {
    Map<String, String> data = new HashMap<>();
    data.put("test", "1");
    data.put("demo", "2");

    String paired = StringUtils.pair(data);

    Assert.assertNotNull(paired);
    Assert.assertFalse(paired.isEmpty());
    Assert.assertTrue(paired.contains("="));
    Assert.assertTrue(paired.contains("&"));
  }

  public void pairInvalid() {
    String paired = StringUtils.pair(new HashMap<>());

    Assert.assertNotNull(paired);
    Assert.assertFalse(paired.isEmpty());
  }

  public void sortedPair() {
    String paired = StringUtils.sortedPair("test=1&demo=2");

    Assert.assertNotNull(paired);
    Assert.assertFalse(paired.isEmpty());
    Assert.assertEquals(paired.indexOf("demo"), 0);
  }

  public void split() {
    String paired = "test=1&demo=2";
    Map<String, String> data = StringUtils.split(paired);

    Assert.assertNotNull(data);
    Assert.assertFalse(data.isEmpty());
    Assert.assertEquals(data.size(), 2);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void splitInvalid() {
    StringUtils.split("test=1&&demo=2");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void splitInvalid2() {
    StringUtils.split("test=1&demo=2232=");
  }

  public void convert2Map() {
    StringUtils.convert2Map("test=1&demo=2232=");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void convert2MapInvalid1() {
    StringUtils.convert2Map("test=1&&demo=11");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void convert2MapInvalid2() {
    StringUtils.convert2Map("test=1&demo");
  }

  public void splitByAlphabet() {
    String paired = "test=1&demo=2";
    SortedMap<String, String> data = (SortedMap<String, String>) StringUtils.splitByAlphabet(paired);

    Assert.assertNotNull(data);
    Assert.assertFalse(data.isEmpty());
    Assert.assertEquals(data.size(), 2);
    Assert.assertEquals(data.firstKey(), "demo");
  }

  public void encodeInvalid() {
    String origin = "xxx";
    String result = StringUtils.encode(origin);

    Assert.assertEquals(result, origin);
  }

  private enum TestEnum {
    JOIN_TEST_ENUM
  }

  public void join() {
    Object object = new Object() {
      @Override
      public String toString() {
        return "WORLD";
      }
    };
    String results = StringUtils.join(new Object[]{"HELLO", object, TestEnum.JOIN_TEST_ENUM, 0});
    Assert.assertEquals(results, "'HELLO','WORLD','JOIN_TEST_ENUM','0'");
  }

  public void joinAndSplitWithComma() {
    List<String> values = Arrays.asList("1", "2", "3");
    String result = StringUtils.joinWithComma(values);
    Assert.assertEquals(result, "1,2,3");

    List<String> expectedValues = StringUtils.splitWithComma(result);
    Assert.assertEquals(values, expectedValues);

    result = StringUtils.joinWithComma(Collections.emptyList());
    Assert.assertEquals(result, StringUtils.EMPTY);

    expectedValues = StringUtils.splitWithComma(StringUtils.EMPTY);
    Assert.assertTrue(expectedValues.isEmpty());
  }

  public void adaptColumnLength() {
    String value = null;
    Assert.assertTrue(null == StringUtils.adapt(value, 3));
    value = "666";
    Assert.assertTrue("666".equals(StringUtils.adapt(value, 3)));
    value = "666666";
    Assert.assertTrue("666".equals(StringUtils.adapt(value, 3)));
  }
}
