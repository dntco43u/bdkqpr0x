package com.fhy8vp3u.bdkqpr0x.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Bdkqpr0xJobListener extends JobExecutionListenerSupport {
  @Override
  public void beforeJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.STARTED) {
      log.info("jobExecution.STARTED");
    }
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("jobExecution.COMPLETED");
    } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
      log.error("jobExecution.FAILED");
    }
  }
}
