package com.easy.quartz.common;

import lombok.extern.log4j.Log4j;
import org.quartz.*;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.Calendar;

@Log4j
public abstract class JobBase implements InterruptableJob {

  protected boolean isConcurrent = true;
  protected boolean interrupt = false;
  private String group = "";
  private String name = "";

  @Override
  public final void execute(JobExecutionContext context) throws JobExecutionException {
    TriggerKey key = context.getTrigger().getKey();
    group = key.getGroup();
    name = key.getName();
    if (!isConcurrent) {
      try {
        context.getScheduler().pauseTrigger(key);
      } catch (SchedulerException ex) {
        log.error(String.format("Quartz. Ошибка остановки триггера на время выполнения задачи \"%1$s\" (группа \"%2$s\") ", name, group), ex);
        throw new JobExecutionException(ex);
      }
    }

    log.debug(String.format("Quartz. Подготовка задачи \"%1$s\" (группа \"%2$s\") ", name, group));

    // Установка бинов
    try {
      BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
      MutablePropertyValues pvs = new MutablePropertyValues();
      pvs.addPropertyValues(context.getScheduler().getContext());
      pvs.addPropertyValues(context.getMergedJobDataMap());
      bw.setPropertyValues(pvs, true);
    } catch (SchedulerException ex) {
      throw new JobExecutionException(ex);
    }

    log.info(String.format("Quartz. Валидация возможности выполнения задачи \"%1$s\" (группа \"%2$s\") ", name, group));

    try {
      validation();
    } catch (Exception ex) {
      log.error(String.format("Quartz. Задача не может быть выполнена \"%1$s\" (группа \"%2$s\") ", name, group), ex);
      return;
    }

    try {

      log.info(String.format("Quartz. Запущена задача \"%1$s\" (группа \"%2$s\") ", name, group));
      long startTime = Calendar.getInstance().getTimeInMillis();
      executeInternal(context);
      long endTime = Calendar.getInstance().getTimeInMillis();
      log.info(String.format("Quartz.  Завершена задача \"%1$s\" (группа \"%2$s\"). Время выполнения %3$s секунд", name, group, (endTime - startTime) / 1000));
    } catch (Exception ex) {
      log.error(String.format("Quartz. Ошибка во время выполнения задачи \"%1$s\" (группа \"%2$s\") ", name, group), ex);
    } finally {
      if (!isConcurrent) {
        try {
          context.getScheduler().resumeTrigger(key);
        } catch (SchedulerException ex) {
          log.error(String.format("Quartz. Ошибка старта триггера после выполнения задачи \"%1$s\" (группа \"%2$s\") ", name, group), ex);
          throw new JobExecutionException(ex);
        }
      }
    }
  }

  @Override
  public void interrupt() throws UnableToInterruptJobException {
    interrupt = true;
    log.info(String.format("Quartz. Получена команда прерывания задачи \"%1$s\" (группа \"%2$s\") ", name, group));
    interruptInternal();
  }

  protected abstract void validation() throws Exception;

  protected abstract void executeInternal(JobExecutionContext context) throws Exception;

  protected void interruptInternal() throws UnableToInterruptJobException {
  }
}
