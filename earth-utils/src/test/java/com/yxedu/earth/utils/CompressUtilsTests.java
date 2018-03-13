package com.yxedu.earth.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;


@Test
public class CompressUtilsTests {

  private final String folderPath = "/tmp/reconciliation/";
  private final String fileName = "555.csv";

  /**
   * 对62MB的文本文件测试,zip压缩后2.9MB内,时间1s内;
   */
  @Test(enabled = false)
  public void compress2Zip() {
    File zipFile = new File(folderPath + fileName + ".zip");
    FileUtils.compress2Zip(new File(folderPath + fileName), zipFile);
    FileUtils.decompressZip(zipFile, new File(folderPath));
  }

  public void getExtensionName() {
    String ext1 = FileUtils.getExtensionName("asdasd");
    Assert.assertTrue(StringUtils.EMPTY.equals(ext1));

    String ext2 = FileUtils.getExtensionName("asdasd.zip");
    Assert.assertTrue("zip".equals(ext2));

    String ext3 = FileUtils.getExtensionName("asdasd.");
    Assert.assertTrue(StringUtils.EMPTY.equals(ext3));
  }
}
