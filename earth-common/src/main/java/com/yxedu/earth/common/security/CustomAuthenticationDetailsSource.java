package com.yxedu.earth.common.security;

import com.yxedu.earth.common.Constants;

import lombok.Getter;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class CustomAuthenticationDetailsSource
    implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

  @Override
  public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
    return new CustomWebAuthenticationDetails(context);
  }

  @Getter
  public static class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private String captcha;
    private String captchaInSession;

    CustomWebAuthenticationDetails(HttpServletRequest request) {
      super(request);
      this.captcha = request.getParameter(Constants.CAPTCHA_PARAMETER_KEY);
      this.captchaInSession = (String) request.getSession()
          .getAttribute(Constants.CAPTCHA_SESSION_KEY);
    }
  }
}
