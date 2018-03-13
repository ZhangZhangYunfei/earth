package com.yxedu.earth.utils;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * {@link EnhancedCronExpression}增强表达时间的设定.
 * 1. 可以使用常规的Cron表达式.
 * 2. 可以使用支持节假日/工作日设定的Cron表达式
 * 3. 可以使用精确的时间而非Cron表达式
 */
@Data
@Builder
public class EnhancedCronExpression {
  //使用精确的时间还是Corn表达式.
  private boolean preciseTimeUsed;
  private boolean cronExpressionUsed;
  private LocalDateTime preciseDateTime;
  private String basicCronExpression;
  //以下设定是对Corn表达式的补充, 是否选择的Cron表达式支持节假日/工作日
  private boolean workdayIncluded;
  private boolean holidayIncluded;
}
