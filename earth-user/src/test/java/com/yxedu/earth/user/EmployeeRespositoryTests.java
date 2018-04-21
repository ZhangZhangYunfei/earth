package com.yxedu.earth.user;

import com.yxedu.earth.user.domain.MerchantEmployee;
import com.yxedu.earth.user.repository.MerchantEmployeeRepository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeRespositoryTests {

  @Autowired
  private MerchantEmployeeRepository repository;

  @Test
  public void shouldSaveAndFindEmployee() {
    MerchantEmployee employee = new MerchantEmployee();
    employee.setMerchantId(1L);
    employee.setEmployeeId(1L);
    employee.setCreatedTime(LocalDateTime.now());
    employee.setUpdatedTime(LocalDateTime.now());

    MerchantEmployee saved = repository.save(employee);
    Assert.assertNotNull(saved.getId());

    List<Object[]> found = repository.findEmployees(employee.getMerchantId());
    Assert.assertFalse(found.isEmpty());
  }
}
