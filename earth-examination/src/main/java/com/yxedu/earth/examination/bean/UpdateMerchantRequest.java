package com.yxedu.earth.examination.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateMerchantRequest {
  @NotNull
  private Long id;

  private String name;

  private String telephone;

  private String address;

  private String description;

  private String contactPerson;

  private boolean disable;
}
