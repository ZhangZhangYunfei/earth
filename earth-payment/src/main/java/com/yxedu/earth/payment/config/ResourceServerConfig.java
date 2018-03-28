package com.yxedu.earth.payment.config;

import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.utils.json.JsonProviderHolder;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        .accessDeniedHandler(new BaseAccessDeniedHandler())
        .tokenServices(getTokenService())
        .stateless(false)
        .authenticationEntryPoint(new RestAuthenticationEntryPoint());
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .exceptionHandling().accessDeniedHandler(new BaseAccessDeniedHandler())
        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
        .and() // session
        .securityContext()
        .securityContextRepository(new HttpSessionSecurityContextRepository());

    http.authorizeRequests()
        //.expressionHandler(new OAuth2WebSecurityExpressionHandler())
        .anyRequest()
        .permitAll();
  }

  private ResourceServerTokenServices getTokenService() {
    DefaultTokenServices services = new DefaultTokenServices();
    services.setTokenStore(new JdbcTokenStore(dataSource));
    return services;
  }

  private static class BaseAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
        throws IOException, ServletException {
      log.error("Failed to access the system.", accessDeniedException);
      enhanceResponse(response, "FAILED", HttpServletResponse.SC_FORBIDDEN,
          "无访问权限！");
    }
  }

  private static class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
      log.error("Only login and authenticated user can access the system.", authException);
      enhanceResponse(response, "FAILED", HttpServletResponse.SC_UNAUTHORIZED,
          "需要登录或者授权才能使用！");
    }
  }

  private static void enhanceResponse(HttpServletResponse response,
                                      String status,
                                      int code,
                                      String msg) throws IOException {
    UniformResponse apiResponse = UniformResponse.builder()
        .status(status)
        .code(Integer.toString(code))
        .message(msg)
        .build();
    response.setStatus(code);
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.getWriter().write(JsonProviderHolder.JACKSON.toJsonString(apiResponse));
  }
}

