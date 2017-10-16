package com.easy.quartz.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormatter {
  private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

  public static SimpleDateFormat getDateFormat(){
    return  simpleDateFormat;
  }

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
}
