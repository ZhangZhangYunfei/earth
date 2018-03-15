package com.yxedu.earth.user.endpoint;

import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.common.exception.EarthException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.criteria.CriteriaBuilder.In;

@Slf4j
@ControllerAdvice
public class EndpointAdvice {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<UniformResponse> handleClientRegistrationException(Exception e)
      throws Exception {
    log.error("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
    if (e instanceof EarthException) {
      EarthException err = (EarthException) e;
      // TODO: 根据exception的code，转义相应的message以及国际化
      return new ResponseEntity<>(
          UniformResponse.failed(Integer.toString(err.getCode()), e.getMessage()),
          HttpStatus.OK);
    }

    return new ResponseEntity<>(UniformResponse.failed("-1", "内部错误！"),
        HttpStatus.OK);
  }
}
