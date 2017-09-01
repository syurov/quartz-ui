package com.easy.quartz.common;

import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.text.ParseException;

public class StringToTimestampConverter implements Converter<String, Timestamp> {
  private final Logger logger = Logger.getLogger(StringToTimestampConverter.class.getName());

  @Override
  public Timestamp convert(String source) {
    try {
      return DateFormatter.parse(source);
    } catch (ParseException e) {
      logger.error(e);
    }
    return null;
  }
}
