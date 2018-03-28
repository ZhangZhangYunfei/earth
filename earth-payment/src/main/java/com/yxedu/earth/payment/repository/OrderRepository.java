package com.yxedu.earth.payment.repository;

import com.yxedu.earth.payment.domain.Order;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {
  Optional<Order> findByMerchantIdAndNo(Long merchantId, String no);
}
