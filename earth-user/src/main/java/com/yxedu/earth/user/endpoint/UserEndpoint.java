package com.yxedu.earth.user.endpoint;

import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.user.bean.RegisterUserRequest;
import com.yxedu.earth.user.bean.UpdateUserRequest;
import com.yxedu.earth.user.domain.User;
import com.yxedu.earth.user.service.AuthenticationHelper;
import com.yxedu.earth.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/users",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserEndpoint {
  private static final String KEY_ID = "id";
  private static final String KEY_USERNAME = "username";

  private final UserService service;
  private final AuthenticationHelper authenticationHelper;

  public UserEndpoint(UserService service, AuthenticationHelper authenticationHelper) {
    this.service = service;
    this.authenticationHelper = authenticationHelper;
  }

  @GetMapping
  public UniformResponse index() {
    return UniformResponse.success(authenticationHelper.getCurrentUser());
  }

  /**
   * Free to visit.
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public UniformResponse register(@RequestBody @Valid RegisterUserRequest user) {
    // todo 注册需要填写以及检查验证码（随机生成或者通过短信发送）
    log.info("Registering the user '{}'...", user.getIdNo());
    String id = service.registerUser(user.getIdNo(), user.getTelephone(), user.getUsername(),
        user.getPassword(), Collections.singletonList("ROLE_EXAMINEE"))
        .getId().toString();
    log.info("Registered the user '{}'.", user.getIdNo());

    Map<String, String> resultMap = new HashMap<>(2);
    resultMap.put(KEY_ID, id);
    resultMap.put(KEY_USERNAME, user.getIdNo());

    return UniformResponse.success(resultMap);
  }

  /**
   * Only system admin is allowed to visit.
   */
  @PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public UniformResponse update(@RequestBody @Valid UpdateUserRequest user) {
    log.info("Updating the user '{}'...", user.getIdNo());
    User updatedUser = service.updateUser(user.getIdNo(), user.getTelephone(),
        user.getUsername(), user.getPassword(), user.getAuthorities(), user.isEnabled(),
        user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked());
    log.info("Updated the user '{}'.", user.getIdNo());

    Map<String, String> resultMap = new HashMap<>(2);
    resultMap.put(KEY_ID, updatedUser.getId().toString());
    resultMap.put(KEY_USERNAME, user.getIdNo());

    return UniformResponse.success(resultMap);
  }

  @PostMapping("password-resets")
  public UniformResponse resetPassword(@NotEmpty @RequestParam("oldPassword") String oldPassword,
                                       @NotEmpty @RequestParam("newPassword") String newPassword) {
    String idNo = authenticationHelper.getIdNo();
    log.info("Changing password of user '{}'...", idNo);
    String id = service.resetPassword(idNo, oldPassword, newPassword);
    log.info("Changed password of user '{}'.", idNo);

    Map<String, String> resultMap = new HashMap<>(2);
    resultMap.put(KEY_ID, id);
    resultMap.put(KEY_USERNAME, idNo);

    return UniformResponse.success(resultMap);
  }
}
