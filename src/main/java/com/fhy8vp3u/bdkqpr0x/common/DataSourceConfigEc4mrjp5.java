package com.fhy8vp3u.bdkqpr0x.common;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.datasource-ec4mrjp5")
@Setter
public class DataSourceConfigEc4mrjp5 {
  private final SSHTunnelConfig initializer; 
  private String driverClassName;
  private String jdbcUrl;
  private String username;
  private String password;

  @Bean(name = "dataSourceEc4mrjp5")
  public DataSource dataSourceEc4mrjp5() {
    log.info("driverClassName={}, jdbcUrl={}, username={}, password={}", driverClassName, jdbcUrl, username, password);
    Map<String, String> paramMap = null;
    try {
      paramMap = initializer.init();
    } catch (Exception e) {
      log.error("", e);
    }
    String targetUrl = jdbcUrl;
    String forwardedUrl = jdbcUrl.replace(paramMap.get("tunnelRemotePort"), paramMap.get("forwardedPort"));    
    log.info("forwarded {} -> {}", targetUrl, forwardedUrl);
    return DataSourceBuilder
    .create()
    .type(HikariDataSource.class)
    .url(forwardedUrl)
    .username(username)
    .password(password)
    .driverClassName(driverClassName)
    .type(HikariDataSource.class)
    .build();
  }  

  @Bean(name = "sqlSessionFactoryEc4mrjp5")
  public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSourceEc4mrjp5") DataSource dataSourceEc4mrjp5,
  ApplicationContext applicationContext) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSourceEc4mrjp5);
    sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("sqlmap/**/*Ec4mrjp5Map.xml"));
    sqlSessionFactoryBean.setTypeAliasesPackage("**.domain");
    sqlSessionFactoryBean
    .setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
    return sqlSessionFactoryBean.getObject();
  }

  @Bean(name = "sqlSessionTemplateEc4mrjp5")
  public SqlSessionTemplate sqlSessionTemplate(
  @Qualifier("sqlSessionFactoryEc4mrjp5") SqlSessionFactory sqlSessionFactoryEc4mrjp5) {
    return new SqlSessionTemplate(sqlSessionFactoryEc4mrjp5, ExecutorType.BATCH);
  }

  @Bean(name = "transactionManagerEc4mrjp5")
  public PlatformTransactionManager transactionManager(
  @Qualifier("dataSourceEc4mrjp5") DataSource dataSourceEc4mrjp5) {
    return new DataSourceTransactionManager(dataSourceEc4mrjp5);
  }
}
