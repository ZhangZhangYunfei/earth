package com.yxedu.earth.examination.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

@EnableResourceServer
@Configuration
@Slf4j
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
  @Value("${spring.application.name}")
  private String appName;

  @Autowired
  private DataSource dataSource;

  @Override
  public void configure(ResourceServerSecurityConfigurer configurer) throws Exception {
    configurer
        .resourceId(appName)
        .expressionHandler(new OAuth2WebSecurityExpressionHandler())
//        .accessDeniedHandler()
//        .tokenStore(new JdbcTokenStore(dataSource))
        .tokenServices(getTokenService())
        .stateless(false);
  }

  private ResourceServerTokenServices getTokenService() {
    DefaultTokenServices services = new DefaultTokenServices();
    services.setTokenStore(new JdbcTokenStore(dataSource));
    //services.setTokenEnhancer(new JwtAccessTokenConverter());
    return services;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().authenticated();
  }
}
