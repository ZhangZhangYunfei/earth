package com.yxedu.earth.user.config;

import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.utils.json.JsonProviderHolder;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@EnableResourceServer
@Configuration
@Slf4j
public class ResourcesServerConfig extends ResourceServerConfigurerAdapter {

  @Value("${spring.application.name}")
  private String appName;

  @Override
  public void configure(ResourceServerSecurityConfigurer configurer) throws Exception {
    configurer
        .resourceId(appName)
        .accessDeniedHandler(new BaseAccessDeniedHandler())
        .expressionHandler(new OAuth2WebSecurityExpressionHandler())
        .stateless(false)
        .authenticationEntryPoint(new RestAuthenticationEntryPoint());
  }

  /**
   * Define protection strategy according to different clients and resources.
   */
  @Override
  public void configure(HttpSecurity http) throws Exception {
    configWebSecurity(http);
    configLoginAndLogout(http);
  }

  private void configLoginAndLogout(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .exceptionHandling().accessDeniedHandler(new BaseAccessDeniedHandler())
        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
        .and() // session
        .securityContext()
        .securityContextRepository(new HttpSessionSecurityContextRepository())
        .and() // login config
        .formLogin()
        .loginProcessingUrl("/users/login")
        .successHandler(new LoginSuccessHandler())
        .failureHandler(new LoginFailureHandler())
        .permitAll()
        .and() // logout config
        .logout()
        .logoutUrl("/users/logout")
        .logoutSuccessHandler(new BaseLogoutSuccessHandler());
  }

  private void configWebSecurity(HttpSecurity http) throws Exception {
    ExpressionUrlAuthorizationConfigurer<HttpSecurity>
        .ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
    registry.expressionHandler(new OAuth2WebSecurityExpressionHandler());
    // other APIs are free to access
    registry
        .antMatchers("/users**")
        .authenticated()
        .anyRequest()
        .permitAll();
  }

  private static class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
        throws ServletException, IOException {
      clearAuthenticationAttributes(request);
      enhanceResponse(response, "SUCCESS", HttpServletResponse.SC_OK, "登录成功!");
    }
  }

  private static class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
        throws IOException, ServletException {
      log.error("Failed to login the system.", exception);
      enhanceResponse(response, "FAILED", HttpServletResponse.SC_UNAUTHORIZED,
          "登录失败，用户名或者密码错误！");
    }
  }

  private static class BaseLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication)
        throws IOException, ServletException {
      enhanceResponse(response, "SUCCESS", HttpServletResponse.SC_OK, "登出成功！");
    }
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
