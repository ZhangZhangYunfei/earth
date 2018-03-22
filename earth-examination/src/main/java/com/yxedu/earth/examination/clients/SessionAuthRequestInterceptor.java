package com.yxedu.earth.examination.clients;

import com.google.common.base.Strings;

import com.yxedu.earth.examination.filters.SessionContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class SessionAuthRequestInterceptor implements RequestInterceptor {
  @Override
  public void apply(RequestTemplate template) {
    if (!Strings.isNullOrEmpty(SessionContextHolder.getContext())) {
      template.header("Cookie", SessionContextHolder.getContext());
    }
  }
}
