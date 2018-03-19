package com.yxedu.earth.examination.domain;

import lombok.Getter;

@Getter
public enum  ExaminationStatus {
  REGISTERING("报名中"),
  REGISTRATION_END("报名结束"),
  ROOM_QUERYABLE("考场可查询"),
  SCORE_QUERYABLE("成绩可查询"),
  FINISHED("考试流程结束");

  private String description;

  ExaminationStatus(String description) {
    this.description = description;
  }
}
