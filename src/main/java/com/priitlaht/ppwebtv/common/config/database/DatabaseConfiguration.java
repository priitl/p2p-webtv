package com.priitlaht.ppwebtv.common.config.database;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.priitlaht.ppwebtv.common.ApplicationConstants;
import com.priitlaht.ppwebtv.common.ApplicationProperties;
import com.priitlaht.ppwebtv.common.config.liquibase.AsyncSpringLiquibase;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;

import javax.inject.Inject;
import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableJpaRepositories("com.priitlaht.ppwebtv.backend.repository")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {

  @Inject
  private Environment environment;

  @Autowired(required = false)
  private MetricRegistry metricRegistry;

  @Bean(destroyMethod = "close")
  @ConditionalOnExpression("#{!environment.acceptsProfiles('cloud') && !environment.acceptsProfiles('heroku')}")
  public DataSource dataSource(DataSourceProperties dataSourceProperties, ApplicationProperties applicationProperties) {
    log.debug("Configuring Datasource");
    if (dataSourceProperties.getUrl() == null) {
      log.error("Your database connection pool configuration is incorrect! The application cannot start. " +
        "Please check your Spring profile, current profiles are: {}", Arrays.toString(environment.getActiveProfiles()));
      throw new ApplicationContextException("Database connection pool is not configured correctly");
    }
    HikariConfig config = new HikariConfig();
    config.setDataSourceClassName(dataSourceProperties.getDriverClassName());
    config.addDataSourceProperty("url", dataSourceProperties.getUrl());
    config.addDataSourceProperty("user", dataSourceProperties.getUsername() != null ? dataSourceProperties.getUsername() : "");
    config.addDataSourceProperty("password", dataSourceProperties.getPassword() != null ? dataSourceProperties.getPassword() : "");
    if (metricRegistry != null) {
      config.setMetricRegistry(metricRegistry);
    }
    return new HikariDataSource(config);
  }

  @Bean
  public SpringLiquibase liquibase(DataSource dataSource, DataSourceProperties dataSourceProperties, LiquibaseProperties liquibaseProperties) {
    SpringLiquibase liquibase = new AsyncSpringLiquibase();
    liquibase.setDataSource(dataSource);
    liquibase.setChangeLog("classpath:config/liquibase/master.xml");
    liquibase.setContexts(liquibaseProperties.getContexts());
    liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
    liquibase.setDropFirst(liquibaseProperties.isDropFirst());
    liquibase.setShouldRun(liquibaseProperties.isEnabled());
    if (environment.acceptsProfiles(ApplicationConstants.SPRING_PROFILE_FAST)) {
      if ("org.h2.jdbcx.JdbcDataSource".equals(dataSourceProperties.getDriverClassName())) {
        liquibase.setShouldRun(true);
        log.warn("Using '{}' profile with H2 database in memory is not optimal, you should consider switching to" +
          " MySQL or Postgresql to avoid rebuilding your database upon each start.", ApplicationConstants.SPRING_PROFILE_FAST);
      } else {
        liquibase.setShouldRun(false);
      }
    } else {
      log.debug("Configuring Liquibase");
    }
    return liquibase;
  }

  @Bean
  public Hibernate4Module hibernate4Module() {
    return new Hibernate4Module();
  }
}
