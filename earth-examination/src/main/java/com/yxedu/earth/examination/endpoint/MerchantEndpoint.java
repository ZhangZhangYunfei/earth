package com.yxedu.earth.examination.endpoint;

import com.google.common.base.Strings;

import com.yxedu.earth.common.Constants;
import com.yxedu.earth.common.UniformResponse;
import com.yxedu.earth.common.exception.EarthException;
import com.yxedu.earth.common.security.AuthenticationHelper;
import com.yxedu.earth.examination.bean.CreateMerchantRequest;
import com.yxedu.earth.examination.bean.UpdateMerchantRequest;
import com.yxedu.earth.examination.domain.Merchant;
import com.yxedu.earth.examination.repository.MerchantRepository;
import com.yxedu.earth.examination.service.IntegrityService;

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
import java.util.Optional;

import javax.validation.Valid;

@Slf4j
@RequestMapping(path = "/merchants",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class MerchantEndpoint {

  private static final String KEY_ID = "id";

  private final MerchantRepository repository;
  private final IntegrityService integrityService;

  public MerchantEndpoint(MerchantRepository repository, IntegrityService integrityService) {
    this.repository = repository;
    this.integrityService = integrityService;
  }

  /**
   * Update a merchant.
   */
  @Secured({Constants.ROLE_ADMIN, Constants.ROLE_EXAMINER})
  @PutMapping
  public UniformResponse update(@RequestBody @Valid UpdateMerchantRequest request) {
    log.info("The user {} is updating merchant {}.", AuthenticationHelper.getId(), request.getId());
    integrityService.checkMerchant(request.getId());

    Merchant merchant = repository.findOne(request.getId());
    if (merchant == null) {
      log.error("The merchant '{}' is not exist...", request.getName());
      throw new EarthException("The merchant is is not existed.");
    }
    if (!Strings.isNullOrEmpty(request.getAddress())) {
      merchant.setAddress(request.getAddress());
    }
    if (!Strings.isNullOrEmpty(request.getContactPerson())) {
      merchant.setContactPerson(request.getContactPerson());
    }
    if (!Strings.isNullOrEmpty(request.getDescription())) {
      merchant.setDescription(request.getDescription());
    }
    if (!Strings.isNullOrEmpty(request.getName())) {
      merchant.setName(request.getName());
    }
    if (!Strings.isNullOrEmpty(request.getTelephone())) {
      merchant.setTelephone(request.getTelephone());
    }

    merchant = repository.save(merchant);

    Map<String, Long> resultMap = new HashMap<>(1);
    resultMap.put(KEY_ID, merchant.getId());

    log.info("The user {} updated merchant {}.", AuthenticationHelper.getId(), request.getId());
    return UniformResponse.success(resultMap);
  }

  /**
   * Create a merchant.
   */
  @PostMapping
  @Secured(Constants.ROLE_ADMIN)
  public UniformResponse create(@RequestBody @Valid CreateMerchantRequest request) {
    log.info("The user {} is creating merchant.", AuthenticationHelper.getId());
    Optional<Merchant> optional = repository.findByName(request.getName());

    if (!optional.isPresent()) {
      Merchant merchant = new Merchant();
      BeanUtils.copyProperties(request, merchant);
      merchant.setCreatedTime(LocalDateTime.now());
      merchant.setUpdatedTime(LocalDateTime.now());
      merchant = repository.save(merchant);

      Map<String, Long> resultMap = new HashMap<>(1);
      resultMap.put(KEY_ID, merchant.getId());

      log.info("The user {} created merchant {}.", AuthenticationHelper.getId(), merchant.getId());
      return UniformResponse.success(resultMap);
    }

    log.error("The name '{}' is already existed...", request.getName());
    throw new EarthException("The username is duplicated.");
  }

  @GetMapping("/{id}")
  public UniformResponse get(@PathVariable Long id) {
    log.info("The user {} is querying merchant {}.", AuthenticationHelper.getId(), id);
    Merchant merchant = repository.findOne(id);
    integrityService.checkMerchant(merchant.getId());
    return UniformResponse.success(merchant);
  }
}
