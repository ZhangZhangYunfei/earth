package com.yxedu.earth.user.service;

import com.google.common.base.Strings;

import com.yxedu.earth.user.repository.UserRepository;
import com.yxedu.earth.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByIdNo(username)
        .map(user -> new User(user.getUsername(),
            StringUtils.joinWithComma(Arrays.asList(user.getPasswordHash(), user.getSalt())),
            user.isEnabled(),
            user.isAccountNonExpired(),
            user.isCredentialsNonExpired(),
            user.isAccountNonLocked(),
            AuthorityUtils.createAuthorityList(
                user.getAuthorities().trim().split(StringUtils.COMMA)))
        ).orElseThrow(() -> new UsernameNotFoundException(
            "Could not find the merchant '" + username + "'"));
  }
}
