package com.yxedu.earth.payment.repository;

import com.yxedu.earth.payment.domain.PaymentType;
import com.yxedu.earth.payment.domain.Secret;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SecretRepositoryTests {

  @Autowired
  private SecretRepository repository;

  @Test
  public void canSaveAndFind() {
    Secret secret = new Secret();
    secret.setMerchantId(123L);
    secret.setApiKey("123");
    secret.setAppId("appid");
    secret.setMerchantNo("123");
    secret.setType(PaymentType.WECHATPAY);
    secret.setCreatedTime(LocalDateTime.now());
    secret.setUpdatedTime(LocalDateTime.now());
    Secret saved = repository.save(secret);
    Assert.assertNotNull(saved.getId());

    List<Secret> list = repository.findByMerchantId(secret.getMerchantId());
    Assert.assertFalse(list.isEmpty());

    Optional<Secret> optional = repository.findByMerchantIdAndType(secret.getMerchantId(),
        secret.getType());
    Assert.assertTrue(optional.isPresent());
  }
}
