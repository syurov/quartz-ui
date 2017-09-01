package com.easy.quartz.common;

import lombok.Data;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import java.util.Date;

@Data
public class TriggerDto {
  private static final long serialVersionUID = 1L;

  TriggerKey triggerKey;
  String triggerKeyName;
  String triggerKeyGroup;
  JobKey jobKey;
  String description;
  Date startTime;
  Date endTime;
  Date nextFireTime;
  Date previousFireTime;
  Trigger.TriggerState triggerState;
}