package com.fhy8vp3u.bdkqpr0x;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Bdkqprf1Application extends DefaultBatchConfigurer {

  @Override
  public void setDataSource(DataSource dataSource) {
    // XXX: Configure springboot batch meta table exclusions, temporary exclusions for test
  }

  public static void main(String[] args) {
    System.exit(SpringApplication.exit(SpringApplication.run(Bdkqprf1Application.class, args)));
  }
}