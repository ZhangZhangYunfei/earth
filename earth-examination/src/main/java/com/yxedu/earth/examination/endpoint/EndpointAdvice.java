package com.yxedu.earth.examination.endpoint;

import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.common.exception.ExceptionResponseConverter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class EndpointAdvice {

  /**
   * Exception base handler.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<UniformResponse> handleClientRegistrationException(Exception err) {
    UniformResponse response = ExceptionResponseConverter.get(err);
    return new ResponseEntity<>(response, HttpStatus.valueOf(Integer.valueOf(response.getCode())));
  }
}
