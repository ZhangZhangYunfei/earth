package com.yxedu.earth.user.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@JsonIgnoreProperties(value = {"passwordHash", "salt"})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "idNo")
@ToString(of = {"idNo", "telephone", "username"})
@Entity
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private Long version;

  @Column(unique = true, nullable = false, length = 18)
  private String idNo;

  @Column(unique = true, nullable = false, length = 13)
  private String telephone;

  @Column(nullable = false, length = 60)
  private String username;

  @Column(name = "password_hash", nullable = false, length = 64)
  private String passwordHash;

  @Column(nullable = false, length = 16)
  private String salt;

  @Column(nullable = false)
  private String authorities;

  @Type(type = "yes_no")
  @Column(name = "enabled", nullable = false)
  private boolean enabled = true;

  @Type(type = "yes_no")
  @Column(name = "account_non_expired", nullable = false)
  private boolean accountNonExpired = true;

  @Type(type = "yes_no")
  @Column(name = "credentials_non_expired", nullable = false)
  private boolean credentialsNonExpired = true;

  @Type(type = "yes_no")
  @Column(name = "account_non_locked", nullable = false)
  private boolean accountNonLocked = true;

  @Column(name = "created_time")
  private LocalDateTime createdTime;

  @Column(name = "updated_time")
  private LocalDateTime updatedTime;
}
