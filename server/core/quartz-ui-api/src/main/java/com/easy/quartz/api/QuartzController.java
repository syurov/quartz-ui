package com.easy.quartz.api;

import com.easy.quartz.common.JobDto;
import com.easy.quartz.common.TriggerDto;
import lombok.extern.log4j.Log4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.*;

import static com.easy.quartz.common.RESTservices.JSONContentType;


@RestController
@RequestMapping("/quartz/")
@Log4j
public class QuartzController implements Serializable {

  private static final long serialVersionUID = 1L;
  private Scheduler schedulerBean;

  @Autowired
  public void setSchedulerBean(Scheduler scheduler) {
    schedulerBean = scheduler;
  }


  @RequestMapping(value = "/", method = RequestMethod.GET, produces = JSONContentType)
  public List<TriggerDto> getTriggers() throws SchedulerException {

    List<TriggerDto> triggers = new ArrayList<>(100);
    for (String groupName : schedulerBean.getTriggerGroupNames()) {
      for (TriggerKey triggerKey : schedulerBean.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName))) {
        Trigger trigger = schedulerBean.getTrigger(triggerKey);
        TriggerDto triggerDto = new TriggerDto();
        triggerDto.setJobKey(trigger.getJobKey());
        triggerDto.setTriggerKey(trigger.getKey());
        triggerDto.setTriggerKeyName(trigger.getKey().getName());
        triggerDto.setTriggerKeyGroup(trigger.getKey().getGroup());
        triggerDto.setDescription(trigger.getDescription());
        triggerDto.setStartTime(trigger.getStartTime());
        triggerDto.setEndTime(trigger.getEndTime());
        triggerDto.setNextFireTime(trigger.getNextFireTime());
        triggerDto.setPreviousFireTime(trigger.getPreviousFireTime());
        triggerDto.setTriggerState(schedulerBean.getTriggerState(triggerKey));

        triggers.add(triggerDto);
      }
    }

    Collections.sort(triggers, (o1, o2) -> {
      int comp = o1.getTriggerKeyGroup().compareTo(o2.getTriggerKeyGroup());
      if (comp == 0)
        return o1.getTriggerKeyName().compareTo(o2.getTriggerKeyName());

      return comp;
    });

    return triggers;
  }

  public Date getTime() {
    return new Date();
  }

  @RequestMapping(value = "/pauseAll", method = RequestMethod.GET, produces = JSONContentType)
  public void pauseAll() throws SchedulerException {
    schedulerBean.pauseAll();
  }

  @RequestMapping(value = "/pause", method = RequestMethod.GET, produces = JSONContentType)
  public void pauseTrigger(String triggerKeyName, String triggerKeyGroup) throws SchedulerException {
    schedulerBean.pauseTrigger(new TriggerKey(triggerKeyName, triggerKeyGroup));
  }

  @RequestMapping(value = "/run", method = RequestMethod.GET, produces = JSONContentType)
  public void fireNow(String name, String group) throws SchedulerException {
    JobKey jobKey = new JobKey(name, group);
    schedulerBean.triggerJob(jobKey);
  }

  @RequestMapping(value = "/resumeAll", method = RequestMethod.GET, produces = JSONContentType)
  public void resumeAll() throws SchedulerException {
    schedulerBean.resumeAll();
  }

  @RequestMapping(value = "/resume", method = RequestMethod.GET, produces = JSONContentType)
  public void resumeTrigger(String name, String group) throws SchedulerException {
    schedulerBean.resumeTrigger(new TriggerKey(name, group));
  }

  @RequestMapping(value = "/executingJobs", method = RequestMethod.GET, produces = JSONContentType)
  public List<JobDto> getCurrentlyExecutingJobs() throws SchedulerException {
    List<JobExecutionContext> executingJobs = schedulerBean.getCurrentlyExecutingJobs();
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
