package com.yxedu.earth.payment.repository;

import com.yxedu.earth.payment.domain.PaymentType;
import com.yxedu.earth.payment.domain.Secret;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SecretRepository extends CrudRepository<Secret, Long> {
  Optional<Secret> findByMerchantIdAndType(Long merchantId, PaymentType type);

  List<Secret> findByMerchantId(Long merchantId);
}
