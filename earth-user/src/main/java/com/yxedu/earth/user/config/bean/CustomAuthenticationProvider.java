package com.yxedu.earth.user.config.bean;

import com.yxedu.earth.common.security.CustomAuthenticationDetailsSource.CustomWebAuthenticationDetails;

import lombok.experimental.var;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {
    if (authentication.getDetails() instanceof CustomWebAuthenticationDetails) {
      var details = (CustomWebAuthenticationDetails) authentication.getDetails();
      String captcha = details.getCaptcha();
      String captchaInSession = details.getCaptchaInSession();

      if (captcha == null || captchaInSession == null) {
        throw new CaptchaException("验证码不存在！");
      }

      if (!captcha.equals(captchaInSession)) {
        throw new CaptchaException("验证码错误！");
      }
    }

    //如果后续要有验证密码的provider，这里需要直接返回null
    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

  public class CaptchaException extends InternalAuthenticationServiceException {
    CaptchaException(String msg) {
      super(msg);
    }
  }
}
