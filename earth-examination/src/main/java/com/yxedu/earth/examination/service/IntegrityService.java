package com.yxedu.earth.examination.service;

import com.yxedu.earth.common.exception.EarthException;
import com.yxedu.earth.common.security.AuthenticationHelper;
import com.yxedu.earth.examination.domain.MerchantEmployee;
import com.yxedu.earth.examination.domain.MerchantEmployeeRepository;
import com.yxedu.earth.examination.repository.ExaminationRepository;
import com.yxedu.earth.examination.repository.RegistrationRepository;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class IntegrityService {

  private final MerchantEmployeeRepository employeeRepository;
  private final RegistrationRepository registrationRepository;
  private final ExaminationRepository examinationRepository;

  public IntegrityService(MerchantEmployeeRepository repository,
                          RegistrationRepository registrationRepository,
                          ExaminationRepository examinationRepository) {
    this.employeeRepository = repository;
    this.registrationRepository = registrationRepository;
    this.examinationRepository = examinationRepository;
  }

  public void checkMerchant(Long merchantId) {
    if (AuthenticationHelper.isAdmin()) {
      return;
    }

    Long savedId = employeeRepository.findByEmployeeId(AuthenticationHelper.getId())
        .map(MerchantEmployee::getMerchantId)
        .orElseThrow(() -> new EarthException("商户的员工才可以访问!"));

    if (!Objects.equals(savedId, merchantId)) {
      throw new EarthException("不可以访问其他商户的资源!");
    }
  }

  public void checkExaminee(Long examineeId, Long registrationId) {
    if (AuthenticationHelper.isAdmin()) {
      return;
    }

    if (Objects.equals(AuthenticationHelper.getId(), examineeId)) {
      return;
    }

    if (AuthenticationHelper.isExaminer()) {
      Long merchantId = employeeRepository.findByEmployeeId(AuthenticationHelper.getId())
          .map(MerchantEmployee::getMerchantId)
          .orElseThrow(() -> new EarthException("商户的员工才可以访问!"));
      Long merchantId2 = examinationRepository.findOne(
          registrationRepository
              .findOne(registrationId)
              .getExaminationId())
          .getMerchantId();
      if (Objects.equals(merchantId, merchantId2)) {
        return;
      }

      throw new EarthException("不可以访问非报名考生的资源!");
    }

    throw new EarthException("不可以访问其他考生的资源!");
  }
}
