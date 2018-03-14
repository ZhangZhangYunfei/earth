package com.yxedu.earth.user.config;

import com.yxedu.earth.user.service.DigestPasswordEncoder;
import com.yxedu.earth.user.service.UserService;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

/**
 * Enable authorization server, we can define different kinds of client in this class to support
 * different authorization requirements.
 */
@EnableAuthorizationServer
@Configuration
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
  /**
   * password grant type needs this to be declared explicitly.
   */
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private DataSource dataSource;

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .authenticationManager(authenticationManager)
        .authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource))
        .accessTokenConverter(new JwtAccessTokenConverter())
        .tokenStore(new JdbcTokenStore(dataSource))
        .pathMapping("/oauth/authorize", "/user/oauth/authorize")
        .pathMapping("/oauth/token", "/user/oauth/token")
        .pathMapping("/oauth/confirm_access", "/user/oauth/confirm_access")
        .pathMapping("/oauth/error", "/user/oauth/error")
        .pathMapping("/oauth/check_token", "/user/oauth/check_token")
        .pathMapping("/oauth/token_key", "/user/oauth/token_key");
  }

  /**
   * Define clients here and control their authority here. As currently we use JDBC to operate/save
   * client details, we only provide data source is sufficient.
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
        .withClient("DEFAULT")
        .secret("DEFAULT")
        .authorizedGrantTypes("authorization_code", "refresh_token",
            "password", "implicit", "client_credentials")
        .scopes("DEFAULT")
        .resourceIds("user", "payment", "examination")
        .accessTokenValiditySeconds(1800);
  }
}
