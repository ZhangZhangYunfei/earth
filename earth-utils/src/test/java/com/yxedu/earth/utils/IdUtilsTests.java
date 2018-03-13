package com.yxedu.earth.utils;

import com.google.common.truth.Truth;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class IdUtilsTests {
  public void generateUUIDAsString() {
    String uuid = IdUtils.uuid();

    Truth.assertThat(uuid).isNotNull();
    Truth.assertThat(uuid).isNotEmpty();
    Truth.assertThat(uuid.length()).isEqualTo(36);
  }

  public void generateUUIDWithoutDash() {
    String uuid = IdUtils.uuidWithoutDash();

    Truth.assertThat(uuid).isNotNull();
    Truth.assertThat(uuid).isNotEmpty();
    Truth.assertWithMessage("uuid length should be 32!").that(uuid.length()).isEqualTo(32);
  }

  public void formatId() {
    String id = IdUtils.formatId("222", 6);
    Assert.assertEquals("000222", id);
  }
}
