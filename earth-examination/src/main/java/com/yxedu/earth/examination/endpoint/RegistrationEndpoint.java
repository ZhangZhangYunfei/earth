package com.yxedu.earth.examination.endpoint;

import com.google.common.base.Strings;

import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.common.security.AuthenticationHelper;
import com.yxedu.earth.examination.bean.CreateRegistrationRequest;
import com.yxedu.earth.examination.bean.UpdateRegistrationRequest;
import com.yxedu.earth.examination.clients.PaymentClient;
import com.yxedu.earth.examination.clients.PaymentOrderRequest;
import com.yxedu.earth.examination.domain.Examination;
import com.yxedu.earth.examination.domain.Registration;
import com.yxedu.earth.examination.domain.RegistrationStatus;
import com.yxedu.earth.examination.repository.ExaminationRepository;
import com.yxedu.earth.examination.repository.RegistrationRepository;

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
import org.springframework.web.bind.annotation.RequestParam;
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
  private final ExaminationRepository examinationRepository;
  private final PaymentClient paymentClient;

  /**
   * Constructor.
   */
  public RegistrationEndpoint(RegistrationRepository repository,
                              ExaminationRepository examinationRepository,
                              PaymentClient paymentClient) {
    this.repository = repository;
    this.examinationRepository = examinationRepository;
    this.paymentClient = paymentClient;
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
    Examination examination = examinationRepository.findOne(registration.getExaminationId());
    AuthenticationHelper.checkExamineeIntegrity(registration.getExamineeId(),
        examination.getMerchantId());

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

  /**
   * Get an registration.
   */
  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public UniformResponse get(@PathVariable Long id) {
    log.info("The user {} is querying registration {}.", AuthenticationHelper.getId(), id);
    Registration registration = repository.findOne(id);
    Examination examination = examinationRepository.findOne(registration.getExaminationId());
    AuthenticationHelper.checkExamineeIntegrity(registration.getExamineeId(),
        examination.getMerchantId());

    //支付中的报名查询支付订单
    if (registration.getStatus() == RegistrationStatus.PAYING) {
      UniformResponse response = paymentClient.getOrder(examination.getMerchantId(),
          registration.getPayNo());

      if ("SUCCESS".equals(response.getStatus())
          && response.getContent() instanceof Map) {
        Map content = (Map) response.getContent(); // 终态成功或者失败
        if ("FAILED".equals(content.get("status")) || "SUCCEED".equals(content.get("status"))) {
          registration.setStatus("SUCCEED".equals(content.get("status"))
              ? RegistrationStatus.PAID : RegistrationStatus.CREATED);
          registration = repository.save(registration);
        }
      }
    }
    return UniformResponse.success(registration);
  }

  /**
   * Get pay url of registration.
   */
  @PostMapping("/{id}/payUrl")
  @PreAuthorize("isAuthenticated()")
  public UniformResponse getUrl(@PathVariable Long id, @RequestParam("paymentType") String type) {
    log.info("The user {} is getting pay url of registration {}.",
        AuthenticationHelper.getId(), id);
    Registration registration = repository.findOne(id);
    Examination examination = examinationRepository.findOne(registration.getExaminationId());
    AuthenticationHelper.checkExamineeIntegrity(registration.getExamineeId(),
        examination.getMerchantId());

    if (registration.getStatus() == RegistrationStatus.CREATED
        || registration.getStatus() == RegistrationStatus.PAYING) {
      PaymentOrderRequest request = PaymentOrderRequest.builder()
          .amount(examination.getPrice())
          .description(examination.getDescription())
          .merchantId(examination.getMerchantId())
          .no(Strings.isNullOrEmpty(registration.getPayNo())
              ? Long.toString(System.nanoTime())
              : registration.getPayNo())
          .paymentType(type)
          .productId(registration.getExaminationId().toString())
          .userId(registration.getExamineeId())
          .build();
      UniformResponse response = paymentClient.getPayUrl(request);
      if ("SUCCESS".equals(response.getStatus())) {
        registration.setStatus(RegistrationStatus.PAYING);
        registration.setPayNo(request.getNo());
        repository.save(registration);
      }
      log.info("The user {} gotten pay url of registration {}.",
          AuthenticationHelper.getId(), id);
      return response;
    }
    log.info("The user {} failed to get pay url of registration {}.",
        AuthenticationHelper.getId(), id);
    return UniformResponse.failed("-1", "不可以重复支付！");
  }
}
