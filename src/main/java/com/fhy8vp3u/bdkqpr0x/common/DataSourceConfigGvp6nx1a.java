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
@ConfigurationProperties(prefix = "spring.datasource-gvp6nx1a")
@Setter
public class DataSourceConfigGvp6nx1a {
  private final SSHTunnelConfig initializer; 
  private String driverClassName;
  private String jdbcUrl;
  private String username;
  private String password;

  @Bean(name = "dataSourceGvp6nx1a")
  public DataSource dataSourceGvp6nx1a() {
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

  @Bean(name = "sqlSessionFactoryGvp6nx1a")
  public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSourceGvp6nx1a") DataSource dataSourceGvp6nx1a,
  ApplicationContext applicationContext) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSourceGvp6nx1a);
    sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("sqlmap/**/*Gvp6nx1aMap.xml"));
    sqlSessionFactoryBean.setTypeAliasesPackage("**.domain");
    sqlSessionFactoryBean
    .setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
    return sqlSessionFactoryBean.getObject();
  }

  @Bean(name = "sqlSessionTemplateGvp6nx1a")
  public SqlSessionTemplate sqlSessionTemplate(
  @Qualifier("sqlSessionFactoryGvp6nx1a") SqlSessionFactory sqlSessionFactoryGvp6nx1a) {
    return new SqlSessionTemplate(sqlSessionFactoryGvp6nx1a, ExecutorType.BATCH);
  }

  @Bean(name = "transactionManagerGvp6nx1a")
  public PlatformTransactionManager transactionManager(
  @Qualifier("dataSourceGvp6nx1a") DataSource dataSourceGvp6nx1a) {
    return new DataSourceTransactionManager(dataSourceGvp6nx1a);
  }
}
