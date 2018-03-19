package com.yxedu.earth.common;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UniformResponse {
  private static final String SUCCESS = "SUCCESS";
  private static final String FAILED = "FAILED";
  private String status;
  private String code;
  private String message;
  private Object content;

  /**
   * Make checkstyle happy.
   */
  public static UniformResponse success(Object content) {
    return UniformResponse.builder()
        .status(SUCCESS)
        .content(content)
        .build();
  }

  /**
   * Make checkstyle happy.
   */
  public static UniformResponse failed(String code, String message) {
    return UniformResponse.builder()
        .status(FAILED)
        .code(code)
        .message(message)
        .build();
  }
}
