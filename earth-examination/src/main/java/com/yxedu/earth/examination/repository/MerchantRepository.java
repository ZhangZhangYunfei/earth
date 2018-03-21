package com.yxedu.earth.examination.repository;

import com.yxedu.earth.examination.domain.Merchant;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MerchantRepository extends CrudRepository<Merchant, Long> {
  Optional<Merchant> findByName(String name);
}
