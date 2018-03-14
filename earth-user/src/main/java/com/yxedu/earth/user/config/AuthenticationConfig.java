package com.yxedu.earth.user.config;

import com.yxedu.earth.user.service.DigestPasswordEncoder;
import com.yxedu.earth.user.service.UserService;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  protected void configure(AuthenticationManagerBuilder builder, UserService userService) {
    try {
      builder
          .userDetailsService(userService)
          .passwordEncoder(new DigestPasswordEncoder());
    } catch (Exception err) {
      throw new BeanCreationException("Error initializing the user details service", err);
    }
  }

}
