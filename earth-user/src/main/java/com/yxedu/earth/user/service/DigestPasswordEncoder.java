package com.yxedu.earth.user.service;

import com.yxedu.earth.utils.SecurityExtUtils;
import com.yxedu.earth.utils.StringUtils;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


public class DigestPasswordEncoder implements PasswordEncoder {

  @Override
  public String encode(CharSequence rawPassword) {
    return SecurityExtUtils.digest(rawPassword.toString());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    List<String> passwordAndSalt = StringUtils.splitWithComma(encodedPassword);
    if (passwordAndSalt.size() == 2) {
      String realEncodedPassword = passwordAndSalt.get(0);
      String salt = passwordAndSalt.get(1);
      return SecurityExtUtils.digest(rawPassword.toString(), salt).equals(realEncodedPassword);
    }
    return SecurityExtUtils.digest(rawPassword.toString()).equals(encodedPassword);
  }
}
