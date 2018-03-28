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
@EqualsAndHashCode(of = {"merchantId", "employeeId"})
@ToString
@Entity
@Table(name = "merchant_employee")
public class MerchantEmployee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private Long version;

  @Column(name = "merchant_id", nullable = false)
  private Long merchantId;

  @Column(name = "employee_id", nullable = false)
  private Long employeeId;

  @Column(nullable = false, name = "created_time")
  private LocalDateTime createdTime;

  @Column(nullable = false, name = "updated_time")
  private LocalDateTime updatedTime;
}
