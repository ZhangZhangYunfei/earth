package com.yxedu.earth.user.repository;

import com.yxedu.earth.user.domain.MerchantEmployee;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MerchantEmployeeRepository extends CrudRepository<MerchantEmployee, Long> {
  Optional<MerchantEmployee> findByEmployeeId(Long employeeId);

  @Query(value = FIND_EMPLOYEE, nativeQuery = true)
  List<Object[]> findEmployees(@Param("merchantId") Long merchantId);

  String FIND_EMPLOYEE = "SELECT ee.id, u.id as userId, u.username\n"
      + "FROM merchant_employee ee \n"
      + "LEFT JOIN user u ON ee.employee_id=u.id \n"
      + "WHERE ee.merchant_id = :merchantId\n"
      + "ORDER BY ee.id";
}
