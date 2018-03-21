package com.yxedu.earth.user.service;

import com.yxedu.earth.common.exception.EarthException;
import com.yxedu.earth.user.domain.User;
import com.yxedu.earth.user.repository.UserRepository;
import com.yxedu.earth.utils.SecurityExtUtils;
import com.yxedu.earth.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Register a new user.
   */
  public User registerUser(String idNo,
                           String telephone,
                           String username,
                           String password,
                           List<String> authorities) {
    if (userRepository.findByIdNo(idNo).isPresent()) {
      log.error("The idNo '{}' have already been registered...", idNo);
      throw new EarthException("The idNo have already been registered.");
    }
    if (userRepository.findByTelephone(telephone).isPresent()) {
      log.error("The telephone '{}' have already been registered...", telephone);
      throw new EarthException("The telephone have already been registered.");
    }
    String salt = SecurityExtUtils.generateSalt();
    User user = new User();
    user.setIdNo(idNo);
    user.setUsername(username);
    user.setPasswordHash(SecurityExtUtils.digest(password, salt));
    user.setSalt(salt);
    user.setTelephone(telephone);
    user.setCreatedTime(LocalDateTime.now());
    user.setUpdatedTime(LocalDateTime.now());
    user.setAuthorities(StringUtils.joinWithComma(authorities));
    user = userRepository.save(user);

    return user;
  }

  /**
   * Update the info of user.
   */
  public User updateUser(String idNo,
                         String telephone,
                         String username,
                         String password,
                         List<String> authorities,
                         boolean enabled,
                         boolean accountNonExpired,
                         boolean credentialsNonExpired,
                         boolean accountNonLocked) {
    Optional<User> userPresent = userRepository.findByIdNo(idNo);
    if (!userPresent.isPresent()) {
      log.error("The user '{}' is not existed...", idNo);
      throw new EarthException("The idNo is not existed.");
    }
    User user = userPresent.get();

    if (Objects.nonNull(password)) {
      String salt = SecurityExtUtils.generateSalt();
      user.setPasswordHash(SecurityExtUtils.digest(password, salt));
      user.setSalt(salt);
    }

    if (Objects.nonNull(username)) {
      user.setTelephone(username);
    }

    if (Objects.nonNull(telephone)) {
      user.setTelephone(telephone);
    }

    if (authorities != null && !authorities.isEmpty()) {
      user.setAuthorities(StringUtils.joinWithComma(authorities));
    }

    user.setEnabled(enabled);
    user.setAccountNonExpired(accountNonExpired);
    user.setCredentialsNonExpired(credentialsNonExpired);
    user.setAccountNonLocked(accountNonLocked);
    user.setUpdatedTime(LocalDateTime.now());

    return userRepository.save(user);
  }

  /**
   * Change the password of user.
   */
  public String resetPassword(String idNo,
                              String oldPassword,
                              String newPassword) {
    Optional<User> userPresent = userRepository.findByIdNo(idNo);
    if (!userPresent.isPresent()) {
      log.error("The idNo '{}' is not existed...", idNo);
      throw new EarthException("The username is not existed.");
    }

    User user = userPresent.get();
    if (!SecurityExtUtils.digest(oldPassword, user.getSalt()).equals(user.getPasswordHash())) {
      log.error("The password is incorrect...");
      throw new EarthException("The password is incorrect.");
    }

    String salt = SecurityExtUtils.generateSalt();
    user.setPasswordHash(SecurityExtUtils.digest(newPassword, salt));
    user.setSalt(salt);
    return userRepository.save(user).getId().toString();
  }

  public User findByIdNo(String idNo) {
    return userRepository.findByIdNo(idNo)
        .orElseThrow(() -> new EarthException("The idno is not found."));
  }
}
