package com.yxedu.earth.user.config;

import com.yxedu.earth.user.service.DigestPasswordEncoder;
import com.yxedu.earth.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.FilterInvocation;

@EnableResourceServer
@Configuration
@Slf4j
public class ResourcesServerConfig extends ResourceServerConfigurerAdapter {

  @Value("${spring.application.name}")
  private String appName;

  /**
   * Sets {@code AuthenticationManagerBuilder}.
   */
  @Autowired
  public void globalUserDetails(AuthenticationManagerBuilder builder,
                                UserService userService) {
    try {
      log.error("");
      builder
          .userDetailsService(userService)
          .passwordEncoder(new DigestPasswordEncoder());
    } catch (Exception err) {
      log.error("");
      throw new BeanCreationException("Error initializing the user details service", err);
    }
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer configurer) throws Exception {
    configurer
        .resourceId(appName)
        .expressionHandler(webExpressionHandler())
        .stateless(false);
  }

  /**
   * Define protection strategy according to different clients and resources.
   */
  @Override
  public void configure(HttpSecurity http) throws Exception {

  }

  private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
    return new OAuth2WebSecurityExpressionHandler();
  }
}
