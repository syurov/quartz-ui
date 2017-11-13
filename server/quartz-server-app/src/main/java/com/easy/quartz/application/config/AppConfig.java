package com.easy.quartz.application.config;


import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Application config
 */
@Log4j
@Configuration
@Import(value = {QuartzConfig.class})
@ComponentScan({
    "com.easy.quartz.database",
    "com.easy.quartz.dal",
    "com.easy.quartz.services"})
public class AppConfig {



}
