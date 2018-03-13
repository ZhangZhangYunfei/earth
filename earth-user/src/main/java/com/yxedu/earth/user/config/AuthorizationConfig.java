package com.yxedu.earth.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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

  @Autowired
  private JdbcTokenStore tokenStore;

  @Autowired
  private AuthorizationCodeServices authorizationCodeServices;

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .authenticationManager(authenticationManager)
        .authorizationCodeServices(authorizationCodeServices)
        .accessTokenConverter(accessTokenConverter())
        .tokenStore(tokenStore);
  }

  /**
   * Define clients here and control their authority here. As currently we use JDBC to operate/save
   * client details, we only provide data source is sufficient.
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.jdbc(dataSource)
        .withClient("acme")
        .secret("acmesecret")
        .authorizedGrantTypes("authorization_code", "refresh_token",
            "password").scopes("openid");
  }

  /**
   * Registers the token converter.
   */
  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    return new JwtAccessTokenConverter();
  }

  /**
   * Registers the token services.
   */
  @Bean
  @Autowired
  public TokenStore tokenStore(DataSource dataSource) {
    return new JdbcTokenStore(dataSource);
  }

  @Bean
  @Autowired
  protected AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
    return new JdbcAuthorizationCodeServices(dataSource);
  }
}
