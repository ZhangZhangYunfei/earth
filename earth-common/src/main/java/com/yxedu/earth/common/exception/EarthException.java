package com.yxedu.earth.common.exception;

import com.google.common.base.Strings;

import lombok.Getter;


public class EarthException extends RuntimeException {
  private static final long serialVersionUID = -357169047628747126L;

  @Getter
  private final int code;

  /**
   * Make checkstyle happy.
   */
  public EarthException(String message, int code, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  /**
   * Make checkstyle happy.
   */
  public EarthException(String message, int code) {
    this(message, code, null);
  }

  /**
   * Make checkstyle happy.
   */
  public EarthException(String message) {
    this(message, -1);
  }

  /**
   * Make checkstyle happy.
   */
  public EarthException(String message,Throwable cause) {
    this(message, -1, cause);
  }

  /**
   * Make checkstyle happy.
   */
  public EarthException(int code, Throwable cause) {
    this("", code, cause);
  }

  /**
   * Make checkstyle happy.
   */
  public EarthException(int code) {
    this("", code);
  }
}
