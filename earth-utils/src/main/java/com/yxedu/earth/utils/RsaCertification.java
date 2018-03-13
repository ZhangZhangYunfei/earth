package com.yxedu.earth.utils;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RsaCertification {
  private String pfxFileContent;
  private String pfxPassword;
  private String certFileContent;
}
