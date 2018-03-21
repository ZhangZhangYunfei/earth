package com.yxedu.earth.user.endpoint;

import com.google.code.kaptcha.impl.DefaultKaptcha;

import com.yxedu.earth.common.Constants;
import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.common.exception.EarthException;
import com.yxedu.earth.common.security.AuthenticationHelper;
import com.yxedu.earth.user.bean.RegisterUserRequest;
import com.yxedu.earth.user.bean.UpdateUserRequest;
import com.yxedu.earth.user.domain.User;
import com.yxedu.earth.user.service.UserService;
import com.yxedu.earth.utils.json.JsonProviderHolder;
import com.yxedu.earth.utils.json.jackson.JacksonProvider;

import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/users",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserEndpoint {
  private static final String KEY_ID = "id";
  private static final String KEY_USERNAME = "username";

  private final UserService service;
  private final DefaultKaptcha captcha;

  /**
   * Make checkstyle happy.
   */
  public UserEndpoint(UserService service,
                      DefaultKaptcha kaptcha) {
    this.service = service;
    this.captcha = kaptcha;
  }

  @GetMapping("/hello/a")
  @PreAuthorize("isAuthenticated()")
  public String index2() {
    return JsonProviderHolder.JACKSON.toJsonString(UniformResponse.success("hello"));
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping
  public UniformResponse index() {
    return UniformResponse.success(service.findByIdNo(AuthenticationHelper.getIdNo()));
  }

  /**
   * Free to visit to create a new user with captcha validation required. Call "get captcha" before
   * this api.
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public UniformResponse register(@RequestBody @Valid RegisterUserRequest user,
                                  HttpSession session) {
    try {
      // TODO:通过短信发送
      log.info("Registering the user '{}'...", user.getIdNo());
      Object captcha = session.getAttribute(Constants.CAPTCHA_SESSION_KEY);
      if (Objects.isNull(captcha)) {
        log.error("The captcha is not found.");
        throw new EarthException("The captcha is not found, please get captcha first.");
      }
      if (!user.getCaptcha().equals(captcha)) {
        log.error("The captcha {} is not equal to {}...", user.getCaptcha(), captcha);
        throw new EarthException("The captcha is not right.");
      }
      String id = service.registerUser(user.getIdNo(), user.getTelephone(), user.getUsername(),
          user.getPassword(), Collections.singletonList(Constants.ROLE_EXAMINEE))
          .getId().toString();
      log.info("Registered the user '{}'.", user.getIdNo());

      Map<String, String> resultMap = new HashMap<>(2);
      resultMap.put(KEY_ID, id);
      resultMap.put(KEY_USERNAME, user.getIdNo());

      return UniformResponse.success(resultMap);
    } finally {
       session.removeAttribute(Constants.CAPTCHA_SESSION_KEY);
    }
  }

  /**
   * Only system admin is allowed to visit. Update the user info.
   */
  @Secured(Constants.ROLE_ADMIN)
  @PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public UniformResponse update(@RequestBody @Valid UpdateUserRequest user) {
    log.info("The user {} is updating user {} info.", AuthenticationHelper.getId(), user.getIdNo());
    User updatedUser = service.updateUser(user.getIdNo(), user.getTelephone(),
        user.getUsername(), user.getPassword(), user.getAuthorities(), user.isEnabled(),
        user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked());

    Map<String, String> resultMap = new HashMap<>(2);
    resultMap.put(KEY_ID, updatedUser.getId().toString());
    resultMap.put(KEY_USERNAME, user.getIdNo());

    log.info("The user {} updated user {} info.",
        AuthenticationHelper.getId(), updatedUser.getId());
    return UniformResponse.success(resultMap);
  }

  /**
   * Resets password.
   */
  @PreAuthorize("isAuthenticated()")
  @PostMapping("password-resets")
  public UniformResponse resetPassword(@NotEmpty @RequestParam("oldPassword") String oldPassword,
                                       @NotEmpty @RequestParam("newPassword") String newPassword) {
    String idNo = AuthenticationHelper.getIdNo();
    log.info("The user {} is reseting password.", AuthenticationHelper.getId());
    String id = service.resetPassword(idNo, oldPassword, newPassword);
    log.info("The user {} reset password.", AuthenticationHelper.getId());

    Map<String, String> resultMap = new HashMap<>(2);
    resultMap.put(KEY_ID, id);
    resultMap.put(KEY_USERNAME, idNo);

    return UniformResponse.success(resultMap);
  }

  /**
   * Get a captcha, call this api before some validation api.
   */
  @GetMapping("captcha")
  public void getCaptcha(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setDateHeader("Expires", 0);
    // Set standard HTTP/1.1 no-cache headers.
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
    response.addHeader("Cache-Control", "post-check=0, pre-check=0");
    // Set standard HTTP/1.0 no-cache header.
    response.setHeader("Pragma", "no-cache");
    // Set content type
    response.setContentType("image/jpeg");

    String capText = captcha.createText();

    request.getSession().setAttribute(Constants.CAPTCHA_SESSION_KEY, capText);
    BufferedImage image = captcha.createImage(capText);
    try (var out = response.getOutputStream()) {
      ImageIO.write(image, Constants.CAPTCHA_FORMAT_KEY, out);
    }
  }
}
