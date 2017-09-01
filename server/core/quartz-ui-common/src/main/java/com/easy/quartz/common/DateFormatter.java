package com.easy.quartz.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class DateFormatter {
  public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

  public static final SimpleDateFormat longDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ssZ");

  public static String format(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }
    return simpleDateFormat.format(timestamp);
  }

  public static Timestamp parse(String source) throws ParseException {
    if (source == null)
      return null;
    return new Timestamp(simpleDateFormat.parse(source).getTime());
  }

  public static Timestamp parsel(String source) throws ParseException {
    if (source == null)
      return null;
    return new Timestamp(longDateFormat.parse(source).getTime());
  }
}
