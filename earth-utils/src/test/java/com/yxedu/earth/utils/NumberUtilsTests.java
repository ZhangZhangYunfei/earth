package com.yxedu.earth.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class NumberUtilsTests {

  public void testToBytes(){
    int i =16;
    byte[] bytes = NumberUtils.toBytes(i);
    Assert.assertTrue(bytes[0]==0);
    Assert.assertTrue(bytes[1]==0);
    Assert.assertTrue(bytes[2]==0);
    Assert.assertTrue(bytes[3]==16);

    i = 128;// 1000 0000
    bytes = NumberUtils.toBytes(i);
    Assert.assertTrue(bytes[0]==0);
    Assert.assertTrue(bytes[1]==0);
    Assert.assertTrue(bytes[2]==0);
    Assert.assertTrue(bytes[3]==-128);

    i =384;// 1 1000 0000
    bytes = NumberUtils.toBytes(i);
    Assert.assertTrue(bytes[0]==0);
    Assert.assertTrue(bytes[1]==0);
    Assert.assertTrue(bytes[2]==1);
    Assert.assertTrue(bytes[3]==-128);


    i = 32896;//1000 0000 1000 0000
    bytes = NumberUtils.toBytes(i);
    Assert.assertTrue(bytes[0]==0);
    Assert.assertTrue(bytes[1]==0);
    Assert.assertTrue(bytes[2]==-128);
    Assert.assertTrue(bytes[3]==-128);

    i = 8421504;//1000 0000 1000 0000 1000 0000
    bytes = NumberUtils.toBytes(i);
    Assert.assertTrue(bytes[0]==0);
    Assert.assertTrue(bytes[1]==-128);
    Assert.assertTrue(bytes[2]==-128);
    Assert.assertTrue(bytes[3]==-128);

    i = 1082163328;//1000000 10000000 10000000 10000000
    bytes = NumberUtils.toBytes(i);
    Assert.assertTrue(bytes[0]==64);
    Assert.assertTrue(bytes[1]==-128);
    Assert.assertTrue(bytes[2]==-128);
    Assert.assertTrue(bytes[3]==-128);

    i = 2147483647;//1111111 11111111 11111111 11111111
    bytes = NumberUtils.toBytes(i);
    Assert.assertTrue(bytes[0]==127);
    Assert.assertTrue(bytes[1]==-1);
    Assert.assertTrue(bytes[2]==-1);
    Assert.assertTrue(bytes[3]==-1);
  }

  public void testToInt(){
    byte[] bytes = new byte[]{127,-1,-1,-1};
    int i = NumberUtils.toInt(bytes);
    Assert.assertTrue(i==2147483647);

    bytes = new byte[]{64,-128,-128,-128};
    i = NumberUtils.toInt(bytes);
    Assert.assertTrue(i==1082163328);

    bytes = new byte[]{0,0,0,-128};
    i = NumberUtils.toInt(bytes);
    Assert.assertTrue(i==128);
  }

}
