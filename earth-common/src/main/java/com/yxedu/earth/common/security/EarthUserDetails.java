package com.yxedu.earth.common.security;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EarthUserDetails extends User {
  private final Long id;
  private final String idNo;
  private final String telephone;
  private final Long merchantId;

  /**
   * Constructor.
   */
  public EarthUserDetails(Long id,
                          String idNo,
                          String telephone,
                          String username,
                          String password,
                          boolean enabled,
                          boolean accountNonExpired,
                          boolean credentialsNonExpired,
                          boolean accountNonLocked,
                          Collection<? extends GrantedAuthority> authorities,
                          Long merchantId) {
    super(username, password, enabled, accountNonExpired,
        credentialsNonExpired, accountNonLocked, authorities);
    this.idNo = idNo;
    this.telephone = telephone;
    this.id = id;
    this.merchantId = merchantId;
  }
}
