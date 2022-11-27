package com.fhy8vp3u.bdkqpr0x.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Bdkqpr0xStepListener extends StepExecutionListenerSupport {
  @Override
  public void beforeStep(StepExecution stepExecution) {
    if (stepExecution.getStatus() == BatchStatus.STARTED) {
      log.info("stepExecution.STARTED");
    }
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    if (stepExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("stepExecution.COMPLETED");
    }
    return new ExitStatus("stepExecution.EXIT");
  }
}