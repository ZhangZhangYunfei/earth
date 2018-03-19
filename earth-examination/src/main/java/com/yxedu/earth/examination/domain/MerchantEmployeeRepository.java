package com.yxedu.earth.examination.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MerchantEmployeeRepository extends CrudRepository<MerchantEmployee, Long> {
  Optional<MerchantEmployee> findByEmployeeId(Long employeeId);
}
