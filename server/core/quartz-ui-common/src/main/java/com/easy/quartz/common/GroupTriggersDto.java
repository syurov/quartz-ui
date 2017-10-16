package com.easy.quartz.common;

import lombok.Data;
import org.quartz.Trigger;

import java.util.Set;

@Data
public class GroupTriggersDto {
  String name;
  Set<Trigger> triggers;
}