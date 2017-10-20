package com.easy.quartz.services.logging;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j
@Order(0)
public class PauseTriggerAspect {

  @Pointcut("execution(* org.quartz.Job.execute(..))")
  private void executeMethod() {
  }

  @Pointcut("@annotation(com.easy.quartz.common.anatation.PauseTrigger)")
  public void annotationPauseTrigger() {
  }

  @Around("executeMethod() && annotationPauseTrigger()")
  public void disableTrigger(ProceedingJoinPoint joinPoint) throws Throwable {
    JobExecutionContext context = (JobExecutionContext) joinPoint.getArgs()[0];
    TriggerKey key = context.getTrigger().getKey();
    String group = key.getGroup();
    String name = key.getName();

    try {
      context.getScheduler().pauseTrigger(key);
    } catch (SchedulerException ex) {
      log.error(String.format("Quartz. Ошибка остановки триггера на время выполнения задачи \"%1$s\" (группа \"%2$s\") ", name, group), ex);
      return;
    }

    try {
      joinPoint.proceed();
    } finally {
      try {
        context.getScheduler().resumeTrigger(key);
      } catch (SchedulerException ex) {
        log.error(String.format("Quartz. Ошибка старта триггера после выполнения задачи \"%1$s\" (группа \"%2$s\") ", name, group), ex);
        throw new JobExecutionException(ex);
      }
    }
  }


}
