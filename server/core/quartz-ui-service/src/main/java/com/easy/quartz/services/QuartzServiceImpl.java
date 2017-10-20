package com.easy.quartz.services;

import com.easy.quartz.common.GroupJobsDto;
import com.easy.quartz.common.GroupTriggersDto;
import com.easy.quartz.common.services.QuartzService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuartzServiceImpl implements QuartzService {

  private Scheduler scheduler;

  @Autowired
  public void setScheduler(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  @Override
  public Set<GroupTriggersDto> getTriggers() throws SchedulerException {

    List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
    Set<GroupTriggersDto> groupTriggers = new HashSet<>(triggerGroupNames.size());
    for (String groupName : triggerGroupNames) {
      GroupTriggersDto group = new GroupTriggersDto();
      group.setName(groupName);

      Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.groupEquals(groupName));
      Set<Trigger> triggers = new HashSet<>(triggerKeys.size());
      for (TriggerKey triggerKey : triggerKeys) {
        Trigger trigger = scheduler.getTrigger(triggerKey);
        triggers.add(trigger);
      }

      group.setTriggers(triggers);

      groupTriggers.add(group);
    }

    return groupTriggers;
  }

  @Override
  public Trigger.TriggerState getTriggersState(String name, String group) throws SchedulerException {
    Trigger.TriggerState triggerState = scheduler.getTriggerState(new TriggerKey(name, group));
    return triggerState;
  }

  @Override
  public Set<GroupJobsDto> getJobs() throws SchedulerException {

    List<String> jobGroupNames = scheduler.getJobGroupNames();
    Set<GroupJobsDto> groupJobs = new HashSet<>(jobGroupNames.size());
    for (String jobName : jobGroupNames) {
      GroupJobsDto group = new GroupJobsDto();
      group.setName(jobName);

      Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.groupEquals(jobName));
      Set<JobDetail> jobs = new HashSet<>(jobKeys.size());
      for (JobKey jobKey : jobKeys) {
        JobDetail job = scheduler.getJobDetail(jobKey);
        jobs.add(job);
      }

      group.setJobDetails(jobs);

      groupJobs.add(group);
    }

    return groupJobs;
  }

  @Override
  public void pauseAll() throws SchedulerException {
    scheduler.pauseAll();
  }

  @Override
  public void pauseTrigger(String name, String group) throws SchedulerException {
    scheduler.pauseTrigger(new TriggerKey(name, group));
  }

  @Override
  public void fireNow(String jobName, String jobGroup) throws SchedulerException {
    JobKey jobKey = new JobKey(jobName, jobGroup);
    scheduler.triggerJob(jobKey);
  }

  @Override
  public void resumeAll() throws SchedulerException {
    scheduler.resumeAll();
  }

  @Override
  public void start() throws SchedulerException {
    scheduler.start();
  }

  @Override
  public void shutdown() throws SchedulerException {
    scheduler.shutdown();
  }

  @Override
  public void resumeTrigger(String name, String group) throws SchedulerException {
    scheduler.resumeTrigger(new TriggerKey(name, group));
  }

  //@Override
 // public List<JobDto> getCurrentlyExecutingJobs() throws SchedulerException {
//    List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
//    List<JobDto> jobDtos = new ArrayList<>(executingJobs.size());
//    for (JobExecutionContext executionContext : executingJobs) {
//      JobDto jobDto = new JobDto();
//      jobDto.setJobKey(executionContext.getJobDetail().getKey());
//      JobDataMap jobDataMap = executionContext.getJobDetail().getJobDataMap();
//      if (jobDataMap.get("details") != null)
//        jobDto.setDetails(jobDataMap.get("details").toString());
//      if (jobDataMap.get("progress") != null)
//        jobDto.setProgress((int) jobDataMap.get("progress"));
//      jobDtos.add(jobDto);
//    }
//    return jobDtos;
//  }
}
