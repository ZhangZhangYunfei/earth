package com.yxedu.earth.common.security;

import com.yxedu.earth.common.Constants;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class AuthenticationHelper {
  /**
   * Get current user according the authentication.
   */
  public static EarthUserDetails getCurrentUser() {
    // password or authorization code grant type, we can get the authorized user info.
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof EarthUserDetails) {
      return (EarthUserDetails) principal;
    }
    // client credential grant type can not get user info.
    log.error("Can not get current user for '{}'", principal.toString());
    throw new RuntimeException(principal.toString());
  }

  /**
   * Get the id of user.
   */
  public static Long getId() {
    return getCurrentUser().getId();
  }


  /**
   * Get the id no of user.
   */
  public static String getIdNo() {
    return getCurrentUser().getIdNo();
  }

  /**
   * Get the telephone of user.
   */
  public static String getTelephone() {
    return getCurrentUser().getTelephone();
  }

  /**
   * Get the name of user.
   */
  public static String getName() {
    return getCurrentUser().getUsername();
  }

  /**
   * Is the current user admin or not.
   */
  public static boolean isAdmin() {
    return getCurrentUser()
        .getAuthorities()
        .contains(new SimpleGrantedAuthority(Constants.ROLE_ADMIN));
  }

  /**
   * Is the current user admin or not.
   */
  public static boolean isExaminer() {
    return getCurrentUser()
        .getAuthorities()
        .contains(new SimpleGrantedAuthority(Constants.ROLE_EXAMINER));
  }
}
