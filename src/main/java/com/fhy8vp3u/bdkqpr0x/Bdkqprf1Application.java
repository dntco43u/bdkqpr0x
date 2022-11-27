package com.fhy8vp3u.bdkqpr0x;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Bdkqprf1Application extends DefaultBatchConfigurer {

  @Override
  public void setDataSource(DataSource dataSource) {
    // XXX: disable batch meta table
  }

  public static void main(String[] args) {
    System.exit(SpringApplication.exit(SpringApplication.run(Bdkqprf1Application.class, args)));
  }
}