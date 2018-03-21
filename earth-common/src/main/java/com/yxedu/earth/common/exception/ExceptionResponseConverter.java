package com.yxedu.earth.common.exception;

import com.yxedu.earth.common.UniformResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.AccessDeniedException;

@Slf4j
public class ExceptionResponseConverter {

  public static UniformResponse get(Exception err) {
    log.error("Handling error: " + err.getClass().getSimpleName() + ", " + err.getMessage());
    if (err instanceof EarthException) {
      EarthException earthException = (EarthException) err;
      // TODO: 根据exception的code，转义相应的message以及国际化
      return UniformResponse.failed(Integer.toString(200),
          earthException.getMessage());
    }

    if (err instanceof AccessDeniedException) {
      return UniformResponse.failed(Integer.toString(401),
          err.getMessage());
    }

    return UniformResponse.failed(Integer.toString(500), "内部错误！");
  }
}
