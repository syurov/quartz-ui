package com.easy.quartz.application.config;


import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Application config
 */
@Log4j
@Configuration
//@Import(value = {SecurityConfig.class, QuartzConfig.class})
@ComponentScan({
    "com.easy.quartz.dal",
    "com.easy.quartz.services"})
public class AppConfig {



}
