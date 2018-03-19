package com.yxedu.earth.user.config;

import com.yxedu.earth.common.security.EarthUserDetails;
import com.yxedu.earth.user.config.bean.CustomAuthenticationProvider;
import com.yxedu.earth.user.repository.UserRepository;
import com.yxedu.earth.utils.SecurityExtUtils;
import com.yxedu.earth.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@Order(100)
public class AuthenticationConfig {

  @Autowired
  protected void configure(AuthenticationManagerBuilder builder, UserRepository userRepository) {
    try {
      builder
          // register our own provider for captcha
          .authenticationProvider(new CustomAuthenticationProvider())
          .userDetailsService(username -> {
            Optional<com.yxedu.earth.user.domain.User> user = userRepository.findByIdNo(username);
            if (user.isPresent()) {
              return new EarthUserDetails(user.get().getId(),
                  user.get().getIdNo(),
                  user.get().getTelephone(),
                  user.get().getUsername(),
                  StringUtils.joinWithComma(
                      Arrays.asList(user.get().getPasswordHash(), user.get().getSalt())),
                  user.get().isEnabled(),
                  user.get().isAccountNonExpired(),
                  user.get().isCredentialsNonExpired(),
                  user.get().isAccountNonLocked(),
                  AuthorityUtils.createAuthorityList(
                      user.get().getAuthorities().trim().split(StringUtils.COMMA)));
            }

            user = userRepository.findByTelephone(username);
            if (user.isPresent()) {
              return new EarthUserDetails(user.get().getId(),
                  user.get().getIdNo(),
                  user.get().getTelephone(),
                  user.get().getUsername(),
                  StringUtils.joinWithComma(
                      Arrays.asList(user.get().getPasswordHash(), user.get().getSalt())),
                  user.get().isEnabled(),
                  user.get().isAccountNonExpired(),
                  user.get().isCredentialsNonExpired(),
                  user.get().isAccountNonLocked(),
                  AuthorityUtils.createAuthorityList(
                      user.get().getAuthorities().trim().split(StringUtils.COMMA)));
            }

            log.error("The username {} can is not existed.", username);
            throw new UsernameNotFoundException("Could not find the merchant '" + username + "'");
          })
          .passwordEncoder(new DigestPasswordEncoder());
    } catch (Exception err) {
      throw new BeanCreationException("Error initializing the user details service", err);
    }
  }

  public static class DigestPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
      return SecurityExtUtils.digest(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
      List<String> passwordAndSalt = StringUtils.splitWithComma(encodedPassword);
      if (passwordAndSalt.size() == 2) {
        String realEncodedPassword = passwordAndSalt.get(0);
        String salt = passwordAndSalt.get(1);
        return SecurityExtUtils.digest(rawPassword.toString(), salt).equals(realEncodedPassword);
      }
      return SecurityExtUtils.digest(rawPassword.toString()).equals(encodedPassword);
    }
  }
}
