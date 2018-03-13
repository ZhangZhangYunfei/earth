package com.yxedu.earth.user.repository;

import com.yxedu.earth.user.domain.User;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long>  {
  Optional<User> findByIdNo(String idNo);

  Optional<User> findByTelephone(String telephone);
}
