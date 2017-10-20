package com.easy.quartz.services.example;

import com.easy.quartz.common.anatation.BeanWrap;
import com.easy.quartz.common.anatation.Logging;
import com.easy.quartz.common.anatation.PauseTrigger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.stereotype.Component;

/**
 * Created by syurov on 31.08.2017.
 */
@Component
public class TestJob implements InterruptableJob {

  @Override
  public void interrupt() throws UnableToInterruptJobException {

  }

  @Logging
  @BeanWrap
  @PauseTrigger
  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    int i=0;
  }
}
