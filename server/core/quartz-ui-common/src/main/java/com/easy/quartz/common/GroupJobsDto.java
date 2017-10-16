package com.easy.quartz.common;

import lombok.Data;
import org.quartz.JobDetail;

import java.util.Set;

@Data
public class GroupJobsDto {
  String name;
  Set<JobDetail> jobDetails;
}