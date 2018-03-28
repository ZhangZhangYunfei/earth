package com.yxedu.earth.examination.endpoint;

import com.google.common.base.Strings;

import com.yxedu.earth.common.Constants;
import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.common.exception.EarthException;
import com.yxedu.earth.common.security.AuthenticationHelper;
import com.yxedu.earth.examination.bean.CreateExaminationRequest;
import com.yxedu.earth.examination.bean.UpdateExaminationRequest;
import com.yxedu.earth.examination.domain.Examination;
import com.yxedu.earth.examination.domain.ExaminationStatus;
import com.yxedu.earth.examination.repository.ExaminationRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping(path = "/examinations",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class ExaminationEndpoint {

  private static final String KEY_ID = "id";


  private final ExaminationRepository repository;

  public ExaminationEndpoint(ExaminationRepository repository) {
    this.repository = repository;
  }

  /**
   * Update an examination.
   */
  @Secured({Constants.ROLE_ADMIN, Constants.ROLE_EXAMINER})
  @PutMapping
  public UniformResponse update(@RequestBody @Valid UpdateExaminationRequest request) {
    log.info("The user {} is updating examination {}.",
        AuthenticationHelper.getId(), request.getId());
    Examination examination = repository.findOne(request.getId());
    if (examination == null) {
      log.error("The examination '{}' is not exist...", request.getId());
      throw new EarthException("The examination is is not existed.");
    }
    AuthenticationHelper.checkMerchantIntegrity(examination.getMerchantId());

    if (!Strings.isNullOrEmpty(request.getSubject())) {
      examination.setSubject(request.getSubject());
    }
    if (!Strings.isNullOrEmpty(request.getRequirement())) {
      examination.setRequirement(request.getRequirement());
    }
    if (!Strings.isNullOrEmpty(request.getDescription())) {
      examination.setDescription(request.getDescription());
    }

    examination = repository.save(examination);

    Map<String, Long> resultMap = new HashMap<>(1);
    resultMap.put(KEY_ID, examination.getId());

    log.info("The user {} updated examination {}.",
        AuthenticationHelper.getId(), examination.getId());
    return UniformResponse.success(resultMap);
  }

  /**
   * Create an examination.
   */
  @Secured({Constants.ROLE_ADMIN, Constants.ROLE_EXAMINER})
  @PostMapping
  public UniformResponse create(@RequestBody @Valid CreateExaminationRequest request) {
    log.info("The user {} is creating examination.", AuthenticationHelper.getId());
    AuthenticationHelper.checkMerchantIntegrity(request.getMerchantId());

    Examination examination = new Examination();
    BeanUtils.copyProperties(request, examination);

    examination.setStatus(ExaminationStatus.REGISTERING);
    examination.setCreatedTime(LocalDateTime.now());
    examination.setUpdatedTime(LocalDateTime.now());
    examination = repository.save(examination);

    Map<String, Long> resultMap = new HashMap<>(1);
    resultMap.put(KEY_ID, examination.getId());

    log.info("The user {} created examination {}.",
        AuthenticationHelper.getId(), examination.getId());
    return UniformResponse.success(resultMap);
  }

  /**
   * Get the examination.
   */
  @GetMapping("/{id}")
  public UniformResponse get(@PathVariable Long id) {
    log.info("The user {} is querying examination {}.", AuthenticationHelper.getId(), id);
    Examination examination = repository.findOne(id);
    return UniformResponse.success(examination);
  }

}
