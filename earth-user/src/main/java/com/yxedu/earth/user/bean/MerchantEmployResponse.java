package com.yxedu.earth.user.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MerchantEmployResponse {
  private Long id;
  private Long userId;
  private String username;
}
