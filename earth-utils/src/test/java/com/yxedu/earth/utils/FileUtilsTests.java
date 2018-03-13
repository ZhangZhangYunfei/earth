package com.yxedu.earth.utils;

import lombok.Builder;
import lombok.Data;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Test
public class FileUtilsTests {

  @Test(enabled = false)
  public void writeCsv() {
    FileUtils.writeCsv(genTasks(), "/tmp/braavos/reconciliation/aaa", "id,name\n", "id,name");
  }

  private List<Object> genTasks() {
    List<Object> tasks = new ArrayList<>();
    tasks.add(Task.builder().id("88888888888888888888888888888888").name("第一个").key("xxx").build());
    tasks.add(Task.builder().id("ALL-3423423424-23421703122148001").name("第二个").key("xxx").build());
    return tasks;
  }

  @Builder
  @Data
  private static class Task {
    private String id;
    private String name;
    private String key;
  }
}
