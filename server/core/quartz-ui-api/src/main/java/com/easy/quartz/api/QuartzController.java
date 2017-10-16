package com.easy.quartz.api;

import com.easy.quartz.common.GroupJobsDto;
import com.easy.quartz.common.GroupTriggersDto;
import com.easy.quartz.common.JobDto;
import lombok.extern.log4j.Log4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.easy.quartz.common.RESTservices.JSONContentType;
@RestController
@RequestMapping("/quartz/")
@Log4j
public class QuartzController implements Serializable {

  private Scheduler scheduler;

  @Autowired
  public void setScheduler(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  @RequestMapping(value = "/triggers", method = RequestMethod.GET, produces = JSONContentType)
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
  @RequestMapping(value = "/jobs", method = RequestMethod.GET, produces = JSONContentType)
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



  @RequestMapping(value = "/pauseAll", method = RequestMethod.GET, produces = JSONContentType)
  public void pauseAll() throws SchedulerException {
    scheduler.pauseAll();
  }

  @RequestMapping(value = "/pause", method = RequestMethod.GET, produces = JSONContentType)
  public void pauseTrigger(String triggerKeyName, String triggerKeyGroup) throws SchedulerException {
    scheduler.pauseTrigger(new TriggerKey(triggerKeyName, triggerKeyGroup));
  }

  @RequestMapping(value = "/run", method = RequestMethod.GET, produces = JSONContentType)
  public void fireNow(String name, String group) throws SchedulerException {
    JobKey jobKey = new JobKey(name, group);
    scheduler.triggerJob(jobKey);
  }

  @RequestMapping(value = "/resumeAll", method = RequestMethod.GET, produces = JSONContentType)
  public void resumeAll() throws SchedulerException {
    scheduler.resumeAll();
  }

  @RequestMapping(value = "/resume", method = RequestMethod.GET, produces = JSONContentType)
  public void resumeTrigger(String name, String group) throws SchedulerException {
    scheduler.resumeTrigger(new TriggerKey(name, group));
  }

  @RequestMapping(value = "/executingJobs", method = RequestMethod.GET, produces = JSONContentType)
  public List<JobDto> getCurrentlyExecutingJobs() throws SchedulerException {
    List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
    List<JobDto> jobDtos = new ArrayList<>(executingJobs.size());
    for (JobExecutionContext executionContext : executingJobs) {
      JobDto jobDto = new JobDto();
      jobDto.setJobKey(executionContext.getJobDetail().getKey());
      JobDataMap jobDataMap = executionContext.getJobDetail().getJobDataMap();
      if (jobDataMap.get("details") != null)
        jobDto.setDetails(jobDataMap.get("details").toString());
      if (jobDataMap.get("progress") != null)
        jobDto.setProgress((int) jobDataMap.get("progress"));
      jobDtos.add(jobDto);
    }
    return jobDtos;
  }


}
