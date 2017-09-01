package com.easy.quartz.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Class for start and configure Spring Application
 */
@EnableAutoConfiguration(exclude = {
    MongoAutoConfiguration.class,
    BatchAutoConfiguration.class,
    RabbitAutoConfiguration.class,
    SecurityFilterAutoConfiguration.class})
@EnableTransactionManagement
@ComponentScan(basePackages = "com.easy.quartz.application")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
