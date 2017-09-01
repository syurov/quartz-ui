package com.easy.quartz.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Базовая реализация конфигурации контекста сервлета
 */
@Configuration
@EnableWebMvc
public abstract class WebConfigBase extends WebMvcConfigurerAdapter {


  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();

  }

  @Override
  public void configurePathMatch(PathMatchConfigurer matcher) {
    matcher.setUseRegisteredSuffixPatternMatch(true);
  }


  @Bean
  public WebContentInterceptor webContentInterceptor() {
    WebContentInterceptor interceptor = new WebContentInterceptor();
    interceptor.setCacheSeconds(0);
    interceptor.setUseExpiresHeader(true);
    interceptor.setUseCacheControlHeader(true);
    interceptor.setUseCacheControlNoStore(true);
    return interceptor;
  }


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(webContentInterceptor());
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new StringToTimestampConverter());
  }


  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    builder
        .failOnEmptyBeans(false)
        .dateFormat(DateFormatter.simpleDateFormat);
    MappingJackson2HttpMessageConverter conv = new MappingJackson2HttpMessageConverter(builder.build());
    List<MediaType> supported = new ArrayList<>();
    supported.add(MediaType.APPLICATION_JSON);
    supported.add(MediaType.APPLICATION_OCTET_STREAM);

    conv.setSupportedMediaTypes(supported);

    converters.add(conv);

    HttpMessageConverter stringConv = new StringHttpMessageConverter(StandardCharsets.UTF_8);

    converters.add(stringConv);
  }

}
