package com.yxedu.earth.user.repository;

import com.yxedu.earth.user.domain.Merchant;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MerchantRepository extends CrudRepository<Merchant, Long> {
  Optional<Merchant> findByName(String name);
}
