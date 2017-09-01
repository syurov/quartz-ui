package com.easy.quartz.application.config;

import com.easy.quartz.common.AutowiringSpringBeanJobFactory;
import lombok.extern.log4j.Log4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Log4j
@Configuration
public class QuartzConfig implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext context) {
    applicationContext = context;
  }

  @Bean(destroyMethod = "destroy")
  SchedulerFactoryBean schedulerFactoryBean() throws Exception {
    SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
    schedulerFactoryBean.setAutoStartup(true);
    // старт после 20 секунд
    schedulerFactoryBean.setStartupDelay(5);


    // Инициализация по файлу
    schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));

    AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    schedulerFactoryBean.setJobFactory(jobFactory);

    schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);

    //schedulerFactoryBean.setAutoStartup();

    return schedulerFactoryBean;
  }

}
