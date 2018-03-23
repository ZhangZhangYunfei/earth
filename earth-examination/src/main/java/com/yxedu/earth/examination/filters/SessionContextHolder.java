package com.yxedu.earth.examination.filters;

public class SessionContextHolder {
  private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

  public void clearContext() {
    contextHolder.remove();
  }

  public static String getContext() {
    return contextHolder.get();
  }

  public static void setContext(String context) {
    contextHolder.set(context);
  }

}
