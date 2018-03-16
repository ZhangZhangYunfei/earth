package com.yxedu.earth.user.config;

import com.yxedu.earth.utils.json.JsonProviderHolder;
import com.yxedu.earth.utils.json.jackson.JacksonProvider;

import lombok.Getter;
import lombok.ToString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.IOException;

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
        .exceptionTranslator(new ExceptionTranslator())
        .authenticationManager(authenticationManager)
        .authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource))
        .accessTokenConverter(new JwtAccessTokenConverter())
        .tokenStore(new JdbcTokenStore(dataSource))
        .pathMapping("/oauth/authorize", "/users/oauth/authorize")
        .pathMapping("/oauth/token", "/users/oauth/token")
        .pathMapping("/oauth/confirm_access", "/user/oauth/confirm_access")
        .pathMapping("/oauth/error", "/users/oauth/error")
        .pathMapping("/oauth/check_token", "/users/oauth/check_token")
        .pathMapping("/oauth/token_key", "/users/oauth/token_key");
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
        // other available options: "implicit", "client_credentials"
        .authorizedGrantTypes("authorization_code", "refresh_token", "password")
        .scopes("DEFAULT")
        .resourceIds("user", "payment", "examination")
        .accessTokenValiditySeconds(1800);
  }

  public static class ExceptionTranslator extends DefaultWebResponseExceptionTranslator {
    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {

      // Try to extract a SpringSecurityException from the stacktrace
      Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
      Exception ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(
          OAuth2Exception.class, causeChain);

      if (ase != null) {
        OAuth2Exception exception = (OAuth2Exception) ase;
        return handleOAuth2Exception(new DefaultOAuth2Exception(exception.getMessage(), 400, ase));
      }

      ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,
          causeChain);
      if (ase != null) {
        return handleOAuth2Exception(new DefaultOAuth2Exception("未授权", 401, e));
      }

      ase = (AccessDeniedException) throwableAnalyzer
          .getFirstThrowableOfType(AccessDeniedException.class, causeChain);
      if (ase instanceof AccessDeniedException) {
        return handleOAuth2Exception(new DefaultOAuth2Exception("禁止访问", 403, ase));
      }

      ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer
          .getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain);
      if (ase instanceof HttpRequestMethodNotSupportedException) {
        return handleOAuth2Exception(
            new DefaultOAuth2Exception("禁止的方法", 405, ase));
      }

      return handleOAuth2Exception(new DefaultOAuth2Exception("内部错误", 500, e));
    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) throws IOException {

      int status = e.getHttpErrorCode();
      HttpHeaders headers = new HttpHeaders();
      headers.set("Cache-Control", "no-store");
      headers.set("Pragma", "no-cache");
      if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
        headers.set("WWW-Authenticate",
            String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
      }

      return new ResponseEntity<>(e, headers, HttpStatus.valueOf(status));
    }

    @Getter
    static class DefaultOAuth2Exception extends OAuth2Exception {

      DefaultOAuth2Exception(String msg, int code, Throwable t) {
        super(msg, t);
        this.addAdditionalInformation("status", "FAILED");
        this.addAdditionalInformation("code", Integer.toString(code));
        this.addAdditionalInformation("message", msg);
      }
    }
  }
}
