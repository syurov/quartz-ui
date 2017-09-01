package com.easy.quartz.common;

import lombok.Data;
import org.quartz.JobKey;

@Data
public class JobDto {
  JobKey jobKey;
  String details = "";
  int progress = 0;
}
