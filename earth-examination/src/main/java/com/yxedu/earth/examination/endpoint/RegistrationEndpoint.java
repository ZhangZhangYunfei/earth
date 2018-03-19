package com.yxedu.earth.examination.endpoint;

import com.google.common.base.Strings;

import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.common.security.AuthenticationHelper;
import com.yxedu.earth.examination.bean.CreateRegistrationRequest;
import com.yxedu.earth.examination.bean.UpdateRegistrationRequest;
import com.yxedu.earth.examination.domain.Registration;
import com.yxedu.earth.examination.domain.RegistrationStatus;
import com.yxedu.earth.examination.repository.RegistrationRepository;
import com.yxedu.earth.examination.service.IntegrityService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;


@Slf4j
@RequestMapping(path = "/registrations",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class RegistrationEndpoint {

  private static final String KEY_ID = "id";

  private final RegistrationRepository repository;
  private final IntegrityService integrityService;

  public RegistrationEndpoint(RegistrationRepository repository,
                              IntegrityService integrityService) {
    this.repository = repository;
    this.integrityService = integrityService;
  }

  /**
   * Update an registration.
   */
  @PutMapping
  @PreAuthorize("isAuthenticated()")
  public UniformResponse update(@RequestBody @Valid UpdateRegistrationRequest request) {
    log.info("The user {} is updating registration {}.",
        AuthenticationHelper.getId(), request.getId());
    Registration registration = repository.findOne(request.getId());
    integrityService.checkExaminee(registration.getExamineeId(), request.getId());

    if (!Strings.isNullOrEmpty(request.getOthers())) {
      registration.setOthers(request.getOthers());
    }

    registration = repository.save(registration);

    Map<String, Long> resultMap = new HashMap<>(1);
    resultMap.put(KEY_ID, registration.getId());

    log.info("The user {} updated registration {}.", AuthenticationHelper.getId(), request.getId());
    return UniformResponse.success(resultMap);
  }

  /**
   * Creating an registration.
   */
  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public UniformResponse create(@RequestBody @Valid CreateRegistrationRequest request) {
    log.info("The user {} is creating registration.", AuthenticationHelper.getId());
    Registration registration = new Registration();
    BeanUtils.copyProperties(request, registration);

    registration.setExamineeId(AuthenticationHelper.getId());
    registration.setExamineeIdNo(AuthenticationHelper.getIdNo());
    registration.setExamineeTelephone(AuthenticationHelper.getTelephone());
    registration.setExamineeName(AuthenticationHelper.getName());

    registration.setStatus(RegistrationStatus.CREATED);
    registration.setCreatedTime(LocalDateTime.now());
    registration.setUpdatedTime(LocalDateTime.now());
    registration = repository.save(registration);

    Map<String, Long> resultMap = new HashMap<>(1);
    resultMap.put(KEY_ID, registration.getId());

    log.info("The user {} created registration {}.",
        AuthenticationHelper.getId(), registration.getId());
    return UniformResponse.success(resultMap);
  }

  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public UniformResponse get(@PathVariable Long id) {
    log.info("The user {} is querying registration {}.", AuthenticationHelper.getId(), id);
    Registration registration = repository.findOne(id);
    integrityService.checkExaminee(registration.getExamineeId(), registration.getId());
    return UniformResponse.success(registration);
  }
}
