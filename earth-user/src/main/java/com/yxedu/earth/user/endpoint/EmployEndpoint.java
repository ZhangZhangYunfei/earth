package com.yxedu.earth.user.endpoint;

import com.yxedu.earth.common.Constants;
import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.common.security.AuthenticationHelper;
import com.yxedu.earth.user.bean.AssignEmployRequest;
import com.yxedu.earth.user.bean.MerchantEmployResponse;
import com.yxedu.earth.user.domain.MerchantEmployee;
import com.yxedu.earth.user.repository.MerchantEmployeeRepository;

import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(path = "/employees",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class EmployEndpoint {
  private static final String KEY_ID = "id";

  private final MerchantEmployeeRepository employeeRepository;

  public EmployEndpoint(MerchantEmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Secured({Constants.ROLE_ADMIN, Constants.ROLE_EXAMINER})
  @PostMapping
  UniformResponse create(@RequestBody AssignEmployRequest request) {
    log.info("The user {} is assigning a employee {} to merchant {}.", AuthenticationHelper.getId(),
        request.getEmployeeId(), request.getMerchantId());
    AuthenticationHelper.checkMerchantIntegrity(request.getMerchantId());

    MerchantEmployee employee = new MerchantEmployee();
    employee.setMerchantId(request.getMerchantId());
    employee.setEmployeeId(request.getEmployeeId());
    employee.setCreatedTime(LocalDateTime.now());
    employee.setUpdatedTime(LocalDateTime.now());
    MerchantEmployee saved = employeeRepository.save(employee);

    Map<String, Long> resultMap = new HashMap<>(1);
    resultMap.put(KEY_ID, saved.getId());

    log.info("The user {} assigned a employee {} to merchant {}.", AuthenticationHelper.getId(),
        request.getEmployeeId(), request.getMerchantId());
    return UniformResponse.success(resultMap);
  }

  @DeleteMapping("/{id}")
  @Secured({Constants.ROLE_ADMIN, Constants.ROLE_EXAMINER})
  UniformResponse delete(@PathVariable Long id) {
    log.info("The user {} is deleting an employee relationship.", AuthenticationHelper.getId(), id);

    AuthenticationHelper.checkMerchantIntegrity(employeeRepository.findOne(id).getMerchantId());
    employeeRepository.delete(id);

    Map<String, Long> resultMap = new HashMap<>(1);
    resultMap.put(KEY_ID, id);
    log.info("The user {} deleted an employee relationship.", AuthenticationHelper.getId(), id);
    return UniformResponse.success(resultMap);
  }

  @GetMapping
  @Secured({Constants.ROLE_ADMIN, Constants.ROLE_EXAMINER})
  UniformResponse get(@RequestParam("merchantId") Long merchantId) {
    log.info("The user {} is querying all employees of merchant {}.", AuthenticationHelper.getId(),
        merchantId);
    AuthenticationHelper.checkMerchantIntegrity(merchantId);

    var results = employeeRepository.findEmployees(merchantId)
        .stream()
        .map(t -> MerchantEmployResponse.builder()
            .id(Long.valueOf(t[0].toString()))
            .userId(Long.valueOf(t[1].toString()))
            .username(t[2].toString())
            .build())
        .collect(Collectors.toList());

    log.info("The user {} queried all employees of merchant {}.", AuthenticationHelper.getId(),
        merchantId);
    return UniformResponse.success(results);
  }
}
