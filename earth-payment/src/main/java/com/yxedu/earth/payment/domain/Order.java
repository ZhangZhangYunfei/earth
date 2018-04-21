package com.yxedu.earth.payment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"version"})
@Data
@EqualsAndHashCode(of = {"merchantId", "no", "amount", "paymentType"})
@Entity
@Table(name = "transaction",
    uniqueConstraints = @UniqueConstraint(columnNames = {"merchant_id", "no"}))
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private Long version;

  @Column(name = "merchant_id", nullable = false, length = 36)
  private Long merchantId;

  @Column(length = 32, nullable = false)
  private String no;

  @Column(name = "user_id", nullable = false, length = 36)
  private Long userId;

  @Column(nullable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(length = 18, nullable = false)
  private OrderStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_type", nullable = false, length = 18)
  private PaymentType paymentType;

  @Column(length = 30)
  private String code;

  @Column
  private String message;

  @Column(name = "created_time", nullable = false)
  private LocalDateTime createdTime;

  @Column(name = "executed_time", nullable = false)
  private LocalDateTime executedTime;

  @Column(name = "updated_time", nullable = false)
  private LocalDateTime updatedTime;

//  @Column(name = "callback_url")
//  private String callbackUrl;

  @Column
  private String description;

  @Column
  private String productId;

  @Column
  private String payUrl;

  //@Column(name = "meta_data", length = 511, columnDefinition = "TEXT")
  private transient String metaData;
}
