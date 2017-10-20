package com.easy.quartz.services.logging;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.quartz.JobExecutionContext;
import org.quartz.TriggerKey;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
@Aspect
@Log4j
@Order(1)
public class LoggingAspect {

  @Pointcut("execution(* org.quartz.Job.execute(..))")
  private void executeMethod() {
  }

  @Pointcut("@annotation(com.easy.quartz.common.anatation.Logging)")
  public void annotationLogging() {
  }

  @Around("executeMethod() && annotationLogging()")

  public void log(ProceedingJoinPoint joinPoint) {
    JobExecutionContext context = (JobExecutionContext) joinPoint.getArgs()[0];
    TriggerKey key = context.getTrigger().getKey();
    String group = key.getGroup();
    String name = key.getName();

    try {
      log.info(String.format("Quartz. Запущена задача \"%1$s\" (группа \"%2$s\") ", name, group));
      long startTime = Calendar.getInstance().getTimeInMillis();
      joinPoint.proceed();
      long endTime = Calendar.getInstance().getTimeInMillis();
      log.info(String.format("Quartz.  Завершена задача \"%1$s\" (группа \"%2$s\"). Время выполнения %3$s секунд", name, group, (endTime - startTime) / 1000));
    } catch (Throwable ex) {
      log.error(String.format("Quartz. Ошибка во время выполнения задачи \"%1$s\" (группа \"%2$s\") ", name, group), ex);
    }
  }


}
