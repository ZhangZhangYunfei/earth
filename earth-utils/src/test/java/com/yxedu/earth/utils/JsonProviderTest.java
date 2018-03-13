package com.yxedu.earth.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import com.yxedu.earth.utils.json.JsonProvider;
import com.yxedu.earth.utils.json.JsonProviderHolder;

import lombok.Getter;
import lombok.Setter;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * {@code JsonProviderTest} is the unit test for {@code JacksonProvider}
 */
@Test
public class JsonProviderTest {

  private JsonProvider provider = JsonProviderHolder.JACKSON;

  public void testParseMap() {

    String testString = "{"
        + "'host': 'localhost', "
        + "'port': 21,"
        + "'username': 'lim', "
        + "'password': '3766853', "
        + "'rootDirectory': 'HFReconTest'"
        + "}";

    Map map1 = provider.parse(testString);
    Assert.assertEquals(map1.size(), 5);
    Assert.assertEquals(map1.get("port"), 21);

    Map map2 = provider.parse(testString, String.class, String.class);
    Assert.assertEquals(map2.size(), 5);
    Assert.assertEquals(map2.get("port"), "21");

    Map map3 = provider.parse(testString, TreeMap.class, String.class, String.class);
    Assert.assertTrue(map3 instanceof TreeMap);
    Assert.assertEquals(map3.size(), 5);
    Assert.assertEquals(map3.get("port"), "21");

  }

  public void testParseList() {
    String testString = "[\n" +
        "  {\n" +
        "    \"areaName\": \"北京\",\n" +
        "    \"areaCode\": 1,\n" +
        "    \"cities\": {\n" +
        "      \"北京\": 10\n" +
        "    }\n" +
        "  },\n" +
        "  {\n" +
        "    \"areaName\": \"上海\",\n" +
        "    \"areaCode\": 2,\n" +
        "    \"cities\": {\n" +
        "      \"上海\": 21\n" +
        "    }\n" +
        "  }\n" +
        "]\n";

    List<AreaConfig> areaConfigs = provider.parseList(testString, AreaConfig.class);
    Assert.assertEquals(areaConfigs.size(), 2);
    Assert.assertEquals(areaConfigs.get(0).areaName, "北京");
    Assert.assertEquals(areaConfigs.get(0).getAreaCode(), (Object) 1);
    Assert.assertEquals(areaConfigs.get(0).getCities().size(), 1);
  }

  public void testParseObject() {
    String testString =
        "  {\n" +
            "    \"areaName\": \"北京\",\n" +
            "    \"areaCode\": 1,\n" +
            "    \"cities\": {\n" +
            "      \"北京\": 10\n" +
            "    }\n" +
            "  },\n";
    AreaConfig areaConfig = provider.parse(testString, AreaConfig.class);
    Assert.assertEquals(areaConfig.getAreaName(), "北京");
    Assert.assertEquals(areaConfig.getAreaCode(), (Object) 1);

    // Make sure single quote & no quote is also supported. Becasue in Braavos, there is some
    // unstandard JSON formatter.

    String testStringWithSingleQuote =
        "  {\n" +
            "    'areaName': '北京',\n" +
            "    'areaCode': 1,\n" +
            "    'cities': {\n" +
            "      \'北京\': 10\n" +
            "    }\n" +
            "  },\n";
    AreaConfig areaConfig2 = provider.parse(testStringWithSingleQuote, AreaConfig.class);
    Assert.assertEquals(areaConfig2.getAreaName(), "北京");
    Assert.assertEquals(areaConfig2.getAreaCode(), (Object) 1);

    String testStringWithNoneQuote =
        "  {\n" +
            "    areaName: '北京',\n" +
            "    areaCode: 1,\n" +
            "    cities: {\n" +
            "      北京: 10\n" +
            "    }\n" +
            "  },\n";

    AreaConfig areaConfig3 = provider.parse(testStringWithNoneQuote, AreaConfig.class);
    Assert.assertEquals(areaConfig3.getAreaName(), "北京");
    Assert.assertEquals(areaConfig3.getAreaCode(), (Object) 1);

  }

  public void testConvertObj() {
    AreaConfig areaConfig = new AreaConfig();
    areaConfig.setAreaCode(1);
    areaConfig.setAreaName("北京");
    areaConfig.setCities(ImmutableMap.of("北京", 10));
    String result = provider.convertObj(areaConfig);

    String expectedResult = "{\"areaName\":\"北京\",\"areaCode\":1,\"cities\":{\"北京\":10}}";
    Assert.assertEquals(result, expectedResult);

    Map map = provider.convertObj(areaConfig, Map.class);
    Assert.assertEquals(map.size(), 3);
    Assert.assertEquals(map.get("areaName"), "北京");
    Assert.assertEquals(map.get("areaCode"), 1);
  }

  public void testGetFieldAsText() throws Exception {
    AreaConfig areaConfig = new AreaConfig();
    areaConfig.setAreaCode(1);
    areaConfig.setAreaName("北京");
    areaConfig.setCities(ImmutableMap.of("北京", 10));

    String value = provider.getFieldAsText(areaConfig, "北京");
    Assert.assertEquals(value, "10");

    value = provider.getFieldAsText(areaConfig, "areaName");
    Assert.assertEquals(value, "北京");
  }


  @Getter
  @Setter
  static class AreaConfig {
    private String areaName;
    private Integer areaCode;
    private Map<String, Integer> cities = Maps.newHashMap();
  }


}
