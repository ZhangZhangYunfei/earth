package com.yxedu.earth.examination.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@EqualsAndHashCode(of = {"examinationId", "examineeId"})
@ToString
@Entity
@Table(name = "registration")
public class Registration {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private Long version;

  @Column(name = "examination_id", nullable = false)
  private Long examinationId;

  @Column(name = "examinee_id", nullable = false)
  private Long examineeId;

  @Column(name = "examinee_id_no", nullable = false, length = 18)
  private String examineeIdNo;

  @Column(name = "examinee_telephone", nullable = false, length = 13)
  private String examineeTelephone;

  @Column(name = "examinee_name", nullable = false, length = 13)
  private String examineeName;

  @Column(nullable = false, length = 60)
  private String others;

  @Column(name = "pay_no", length = 32)
  private String payNo;

  @Column(nullable = false, length = 16)
  @Enumerated(EnumType.STRING)
  private RegistrationStatus status;

  @Column(name = "created_time")
  private LocalDateTime createdTime;

  @Column(name = "updated_time")
  private LocalDateTime updatedTime;
}
