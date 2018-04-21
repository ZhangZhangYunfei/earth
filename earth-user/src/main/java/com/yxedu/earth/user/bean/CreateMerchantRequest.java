package com.yxedu.earth.user.bean;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
public class CreateMerchantRequest {
  @NotEmpty
  private String name;

  @NotEmpty
  private String telephone;

  @NotEmpty
  private String address;

  @NotEmpty
  private String description;

  @NotEmpty
  private String contactPerson;

  private boolean disable;
}
