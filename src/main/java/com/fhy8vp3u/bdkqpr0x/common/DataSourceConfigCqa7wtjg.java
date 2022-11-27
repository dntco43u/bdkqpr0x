package com.fhy8vp3u.bdkqpr0x.common;

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
import org.springframework.context.annotation.Primary;
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
@ConfigurationProperties(prefix = "spring.datasource-cqa7wtjg")
@Setter
public class DataSourceConfigCqa7wtjg {
  private String driverClassName;
  private String jdbcUrl;
  private String username;
  private String password;

  @Primary
  @Bean(name = "dataSourceCqa7wtjg")
  @ConfigurationProperties(prefix = "spring.datasource-cqa7wtjg")
  public DataSource dataSourceCqa7wtjg() {
    log.info("driverClassName={}, jdbcUrl={}, username={}, password={}", driverClassName, jdbcUrl, username, password);
    return DataSourceBuilder
    .create()
    .type(HikariDataSource.class)
    .build();
  }

  @Primary
  @Bean(name = "sqlSessionFactoryCqa7wtjg")
  public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSourceCqa7wtjg") DataSource dataSourceCqa7wtjg,
  ApplicationContext applicationContext) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSourceCqa7wtjg);
    sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("sqlmap/**/*Cqa7wtjgMap.xml"));
    sqlSessionFactoryBean.setTypeAliasesPackage("**.domain");
    sqlSessionFactoryBean
    .setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
    return sqlSessionFactoryBean.getObject();
  }

  @Primary
  @Bean(name = "sqlSessionTemplateCqa7wtjg")
  public SqlSessionTemplate sqlSessionTemplate(
  @Qualifier("sqlSessionFactoryCqa7wtjg") SqlSessionFactory sqlSessionFactoryCqa7wtjg) {
    return new SqlSessionTemplate(sqlSessionFactoryCqa7wtjg, ExecutorType.BATCH);
  }

  @Primary
  @Bean(name = "transactionManagerCqa7wtjg")
  public PlatformTransactionManager transactionManager(
  @Qualifier("dataSourceCqa7wtjg") DataSource dataSourceCqa7wtjg) {
    return new DataSourceTransactionManager(dataSourceCqa7wtjg);
  }
}