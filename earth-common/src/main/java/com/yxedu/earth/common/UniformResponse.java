package com.yxedu.earth.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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
