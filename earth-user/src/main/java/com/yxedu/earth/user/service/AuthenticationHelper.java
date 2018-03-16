package com.yxedu.earth.user.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * Helper for authentication.
 */
@Slf4j
@Component
public class AuthenticationHelper {
  /**
   * Get current user according the authentication.
   */
  public User getCurrentUser() {
    // password or authorization code grant type, we can get the authorized user info.
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof User) {
      return (User) principal;
    }
    // client credential grant type can not get user info.
    log.error("Can not get current user for '{}'", principal.toString());
    throw new RuntimeException(principal.toString());
  }


  public String getIdNo() {
    return getCurrentUser().getUsername();
  }
}

