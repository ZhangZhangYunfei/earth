package com.yxedu.earth.payment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

@JsonIgnoreProperties({"version"})
@Data
@EqualsAndHashCode(of = {"type", "merchantId"})
@Entity
@Table(name = "secret",
    uniqueConstraints = @UniqueConstraint(columnNames = {"merchant_id", "type"}))
public class Secret {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private Long version;

  @Column(name = "merchant_id")
  private Long merchantId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 18)
  private PaymentType type;

  @Column(name = "merchant_no", nullable = false, length = 32)
  private String merchantNo;

  @Column(name = "app_id", length = 128)
  private String appId;

  @Column(name = "api_key", length = 6000, columnDefinition = "TEXT")
  private String apiKey;

  @Column(name = "updated_time")
  private LocalDateTime updatedTime;

  @Column(name = "created_time")
  private LocalDateTime createdTime;
}
