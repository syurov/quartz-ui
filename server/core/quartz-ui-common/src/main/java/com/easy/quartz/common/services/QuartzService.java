package com.easy.quartz.common.services;

import com.easy.quartz.common.GroupJobsDto;
import com.easy.quartz.common.GroupTriggersDto;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.util.Set;

public interface QuartzService {
  Set<GroupTriggersDto> getTriggers() throws SchedulerException;

  Trigger.TriggerState getTriggersState(String name, String group) throws SchedulerException;

  Set<GroupJobsDto> getJobs() throws SchedulerException;

  void pauseAll() throws SchedulerException;

  void pauseTrigger(String name, String group) throws SchedulerException;

  void fireNow(String jobName, String jobGroup) throws SchedulerException;

  void resumeAll() throws SchedulerException;

  void start() throws SchedulerException;

  void shutdown() throws SchedulerException;

  void resumeTrigger(String name, String group) throws SchedulerException;

//  List<JobDto> getCurrentlyExecutingJobs() throws SchedulerException;
}
