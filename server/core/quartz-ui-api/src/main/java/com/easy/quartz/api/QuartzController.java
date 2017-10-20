package com.easy.quartz.api;

import com.easy.quartz.common.GroupJobsDto;
import com.easy.quartz.common.GroupTriggersDto;
import com.easy.quartz.common.services.QuartzService;
import lombok.extern.log4j.Log4j;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Set;

import static com.easy.quartz.common.RESTservices.JSONContentType;

@RestController
@RequestMapping("/quartz/")
@Log4j
public class QuartzController implements Serializable {

  @Autowired
  QuartzService quartzService;

  @RequestMapping(value = "/triggers", method = RequestMethod.GET, produces = JSONContentType)
  public Set<GroupTriggersDto> getTriggers() throws SchedulerException {
    return quartzService.getTriggers();
  }

  @RequestMapping(value = "/triggersState", method = RequestMethod.GET, produces = JSONContentType)
  public Trigger.TriggerState getTriggersState(String name, String group) throws SchedulerException {
    return quartzService.getTriggersState(name, group);
  }

  @RequestMapping(value = "/jobs", method = RequestMethod.GET, produces = JSONContentType)
  public Set<GroupJobsDto> getJobs() throws SchedulerException {
    return quartzService.getJobs();
  }

  @RequestMapping(value = "/pauseAll", method = RequestMethod.GET, produces = JSONContentType)
  public void pauseAll() throws SchedulerException {
    quartzService.pauseAll();
  }

  @RequestMapping(value = "/pause", method = RequestMethod.GET, produces = JSONContentType)
  public void pauseTrigger(String name, String group) throws SchedulerException {
    quartzService.pauseTrigger(name, group);
  }

  @RequestMapping(value = "/fire", method = RequestMethod.GET, produces = JSONContentType)
  public void fireNow(String jobName, String jobGroup) throws SchedulerException {
    quartzService.pauseTrigger(jobName, jobGroup);
  }

  @RequestMapping(value = "/resumeAll", method = RequestMethod.GET, produces = JSONContentType)
  public void resumeAll() throws SchedulerException {
    quartzService.resumeAll();
  }

  @RequestMapping(value = "/start", method = RequestMethod.GET, produces = JSONContentType)
  public void start() throws SchedulerException {
    quartzService.start();
  }

  @RequestMapping(value = "/shutdown", method = RequestMethod.GET, produces = JSONContentType)
  public void shutdown() throws SchedulerException {
    quartzService.shutdown();
  }


  @RequestMapping(value = "/resume", method = RequestMethod.GET, produces = JSONContentType)
  public void resumeTrigger(String name, String group) throws SchedulerException {
    quartzService.resumeTrigger(name, group);
  }

//  @RequestMapping(value = "/executingJobs", method = RequestMethod.GET, produces = JSONContentType)
//  public List<JobDto> getCurrentlyExecutingJobs() throws SchedulerException {
//    return quartzService.getCurrentlyExecutingJobs();
//  }
}
