package com.yxedu.earth.user.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString
@Entity
@Table(name = "merchant")
public class Merchant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private Long version;

  @Column(unique = true, nullable = false, length = 60)
  private String name;

  @Column(nullable = false, length = 60)
  private String address;

  @Column(nullable = false, length = 13)
  private String telephone;

  @Column(nullable = false, length = 13)
  private String description;

  @Column(nullable = false, length = 13)
  private String contactPerson;

  @Column(nullable = false, name = "created_time")
  private LocalDateTime createdTime;

  @Column(nullable = false, name = "updated_time")
  private LocalDateTime updatedTime;
}

