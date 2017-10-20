package com.easy.quartz.services.logging;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j
@Order(2)
public class BeanWrapAspect {

  @Pointcut("execution(* org.quartz.Job.execute(..))")
  private void executeMethod() {
  }

  @Pointcut("@annotation(com.easy.quartz.common.anatation.BeanWrap)")
  public void annotationBeanWrap() {
  }

  @Around("executeMethod() && annotationBeanWrap()")
 public void wrap(ProceedingJoinPoint joinPoint) throws Throwable {
    JobExecutionContext context = (JobExecutionContext) joinPoint.getArgs()[0];
    try {
      BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
      MutablePropertyValues pvs = new MutablePropertyValues();
      pvs.addPropertyValues(context.getScheduler().getContext());
      pvs.addPropertyValues(context.getMergedJobDataMap());
      bw.setPropertyValues(pvs, true);
    } catch (SchedulerException ex) {
      throw new JobExecutionException(ex);
    }
  }



}
