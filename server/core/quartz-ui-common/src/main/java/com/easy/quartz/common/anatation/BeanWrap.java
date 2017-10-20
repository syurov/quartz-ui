package com.easy.quartz.common.anatation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface BeanWrap {
  String value() default "";
}
