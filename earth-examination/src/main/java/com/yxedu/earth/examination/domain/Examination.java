package com.yxedu.earth.examination.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
import javax.persistence.Version;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"merchantId", "subject"})
@ToString
@Entity
@Table(name = "examination")
public class Examination {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private Long version;

  @Column(name = "merchant_id", nullable = false)
  private Long merchantId;

  @Column(nullable = false)
  private String subject;

  @Column
  private String requirement;

  @Column
  private String description;

  @Column(nullable = false)
  private BigDecimal price;

  @Column
  @Enumerated(EnumType.STRING)
  private ExaminationStatus status;

  @Column(nullable = false,name = "created_time")
  private LocalDateTime createdTime;

  @Column(nullable = false,name = "updated_time")
  private LocalDateTime updatedTime;
}
