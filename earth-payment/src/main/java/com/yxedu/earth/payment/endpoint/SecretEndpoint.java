package com.yxedu.earth.payment.endpoint;

import com.yxedu.earth.common.Constants;
import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.common.exception.EarthException;
import com.yxedu.earth.common.security.AuthenticationHelper;
import com.yxedu.earth.payment.bean.CreateSecretRequest;
import com.yxedu.earth.payment.bean.UpdateSecretRequest;
import com.yxedu.earth.payment.domain.Secret;
import com.yxedu.earth.payment.repository.SecretRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

@Slf4j
@RequestMapping(path = "/secrets",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class SecretEndpoint {
  private static final String KEY_ID = "id";

  private final SecretRepository secretRepository;

  public SecretEndpoint(SecretRepository secretRepository) {
    this.secretRepository = secretRepository;
  }

  @Secured(Constants.ROLE_ADMIN)
  @PostMapping
  UniformResponse create(@RequestBody @Valid CreateSecretRequest request) {
    AuthenticationHelper.checkMerchantIntegrity(request.getMerchantId());
    Optional<Secret> found = secretRepository.findByMerchantIdAndType(request.getMerchantId(),
        request.getType());
    if (found.isPresent()) {
      log.error("The secret type {} of merchant {} is already existed!",
          request.getType(), request.getMerchantId());
      throw new EarthException("The secret for merchant is already existed.");
    }
    Secret secret = new Secret();
    BeanUtils.copyProperties(request, secret);
    secret.setCreatedTime(LocalDateTime.now());
    secret.setUpdatedTime(LocalDateTime.now());
    Secret saved = secretRepository.save(secret);

    Map<String, Long> resultMap = new HashMap<>(1);
    resultMap.put(KEY_ID, saved.getId());

    return UniformResponse.success(resultMap);
  }

  @Secured(Constants.ROLE_ADMIN)
  @PutMapping
  UniformResponse update(@RequestBody @Valid UpdateSecretRequest request) {
    Secret secret = secretRepository.findOne(request.getId());
    if (secret == null) {
      log.error("The secret {} is not existed!", request.getId());
      throw new EarthException("The secret is not existed.");
    }
    AuthenticationHelper.checkMerchantIntegrity(secret.getMerchantId());
    if (request.getMerchantNo() != null) {
      secret.setMerchantNo(request.getMerchantNo());
    }
    if (request.getAppId() != null) {
      secret.setAppId(request.getAppId());
    }
    if (request.getApiKey() != null) {
      secret.setApiKey(request.getApiKey());
    }
    secret.setUpdatedTime(LocalDateTime.now());
    Secret saved = secretRepository.save(secret);

    Map<String, Long> resultMap = new HashMap<>(1);
    resultMap.put(KEY_ID, saved.getId());

    return UniformResponse.success(resultMap);
  }

  @Secured(Constants.ROLE_ADMIN)
  @GetMapping
  UniformResponse get(@RequestParam("merchantId") Long merchantId) {
    AuthenticationHelper.checkMerchantIntegrity(merchantId);
    return UniformResponse.success(secretRepository.findByMerchantId(merchantId));
  }
}
