package com.yxedu.earth.utils;

import com.google.common.truth.Truth;

import org.awaitility.Awaitility;
import org.testng.annotations.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.IntStream;

@Test
public class IdWorkerTests {
  private IdWorker idWorker = new IdWorker(0, 0);

  @Test
  public void get() {
    Long id = idWorker.nextId();

    Truth.assertThat(id).isNotNull();
    Truth.assertWithMessage("The id length should equals to 18!")
        .that(id.toString().length())
        .isEqualTo(18);
  }

  @Test
  public void multiGet() {
    Set<Long> ids = new ConcurrentSkipListSet<>();

    // just a quick demo for awaitility...
    Awaitility.await()
        .until(Awaitility.matches(() -> IntStream
            .range(0, 300)
            .parallel()
            .forEach(i -> ids.add(idWorker.nextId()))));

    Truth.assertThat(ids).isNotEmpty();
    Truth.assertWithMessage("The size of id set should equals to 300!")
        .that(ids.size())
        .isEqualTo(300);
  }
}
