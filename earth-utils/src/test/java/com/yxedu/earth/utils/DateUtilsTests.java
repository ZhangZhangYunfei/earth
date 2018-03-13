package com.yxedu.earth.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@Test
public class DateUtilsTests {
  public void parse() {
    String date = "20150821";
    LocalDate localDate = DateUtils.parse(date);

    Assert.assertNotNull(localDate);
    Assert.assertEquals(localDate, LocalDate.of(2015, 8, 21));
  }

  public void prettyParse() {
    String date = "2015-08-21";
    LocalDate localDate = DateUtils.parsePretty(date);

    Assert.assertNotNull(localDate);
    Assert.assertEquals(localDate, LocalDate.of(2015, 8, 21));
  }

  public void parseDateTime() {
    String dateTime = "20150821120000";
    LocalDateTime localDateTime = DateUtils.parseDateTime(dateTime);

    Assert.assertNotNull(localDateTime);
    Assert.assertEquals(localDateTime, LocalDateTime.of(2015, 8, 21, 12, 0, 0));
  }

  public void parsePrettyDateTime() {
    String dateTime = "2015-08-21 12:00:00";
    LocalDateTime localDateTime = DateUtils.parsePrettyDateTime(dateTime);

    Assert.assertNotNull(localDateTime);
    Assert.assertEquals(localDateTime, LocalDateTime.of(2015, 8, 21, 12, 0, 0));
  }

  public void parsePrettyPreciseDateTime() {
    String dateTime = "2015-08-21 12:00:00.000";
    LocalDateTime localDateTime = DateUtils.parsePrettyPreciseDateTime(dateTime);

    Assert.assertNotNull(localDateTime);
    Assert.assertEquals(localDateTime, LocalDateTime.of(2015, 8, 21, 12, 0, 0, 0));
  }

  @Test(expectedExceptions = DateTimeParseException.class)
  public void parseInvalidDataTime() {
    String dateTime = "Invalid time data string";
    DateUtils.parsePrettyPreciseDateTime(dateTime);
  }

  public void now() {
    String now = DateUtils.now();
    LocalDate date = DateUtils.parse(now);

    Assert.assertNotNull(date);
    Assert.assertFalse(now.contains("-"));
  }

  public void prettyNow() {
    String now = DateUtils.prettyNow();
    LocalDate date = DateUtils.parsePretty(now);

    Assert.assertNotNull(date);
    Assert.assertTrue(now.contains("-"));
  }

  public void nowTime() {
    String nowTime = DateUtils.nowTime();
    LocalDateTime dateTime = DateUtils.parseDateTime(nowTime);

    Assert.assertNotNull(dateTime);
    Assert.assertFalse(nowTime.contains("-"));
    Assert.assertFalse(nowTime.contains(":"));
  }

  public void prettyNowTime() {
    String nowTime = DateUtils.prettyNowTime();
    LocalDateTime dateTime = DateUtils.parsePrettyDateTime(nowTime);

    Assert.assertNotNull(dateTime);
    Assert.assertTrue(nowTime.contains("-"));
    Assert.assertTrue(nowTime.contains(":"));
  }

  public void preciseNow() {
    String nowTime = DateUtils.preciseNow();

    Assert.assertNotNull(nowTime);
    Assert.assertEquals(nowTime.length(), 17);
    Assert.assertFalse(nowTime.contains("-"));
    Assert.assertFalse(nowTime.contains(":"));
    Assert.assertFalse(nowTime.contains("."));
  }

  public void prettyPreciseNow() {
    String nowTime = DateUtils.prettyPreciseNow();
    LocalDateTime dateTime = DateUtils.parsePrettyPreciseDateTime(nowTime);

    Assert.assertNotNull(dateTime);
    Assert.assertTrue(nowTime.contains("-"));
    Assert.assertTrue(nowTime.contains(":"));
    Assert.assertTrue(nowTime.contains("."));
  }

  public void getYear() {
    String year = DateUtils.getYear();

    Assert.assertNotNull(year);
    Assert.assertTrue(year.startsWith("20"));
  }

  public void getStartOfToday() {
    LocalDateTime start = DateUtils.getStartOfToday();

    Assert.assertNotNull(start);
    Assert.assertEquals(start.toLocalDate(), LocalDate.now());
    Assert.assertEquals(start.toLocalTime(), LocalTime.MIN);
  }

  public void getEndOfToday() {
    LocalDateTime end = DateUtils.getEndOfToday();

    Assert.assertNotNull(end);
    Assert.assertEquals(end.toLocalDate(), LocalDate.now());
    Assert.assertEquals(end.toLocalTime(), LocalTime.MAX);
  }

  public void getLong() {
    LocalDateTime now = LocalDateTime.now();
    Long expectedCurrentMills = System.currentTimeMillis();
    Long actualCurrentMills = DateUtils.getLong(now);
    // Expected the mills calculated in DateUtils is almost equal with system mills.
    // The difference should be less than 1 second.
    Assert.assertTrue(Math.abs(actualCurrentMills - expectedCurrentMills) < 1000);
  }

  public void getFirstDayOfMonth() {
    LocalDateTime start = DateUtils.getFirstDayOfMonth();
    Assert.assertNotNull(start);
    Assert.assertEquals(start.getDayOfMonth(), 1);
    Assert.assertEquals(start.getMonth(), LocalDate.now().getMonth());
  }

  public void getLocalTime() {
    String localTime = "18:20";
    LocalTime time = DateUtils.parseLocalTime(localTime);
    LocalTime expectedTime = LocalTime.of(18, 20);
    Assert.assertEquals(time, expectedTime);
  }
}
