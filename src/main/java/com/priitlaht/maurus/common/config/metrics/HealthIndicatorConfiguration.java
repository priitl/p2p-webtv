package com.priitlaht.maurus.common.config.metrics;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
public class HealthIndicatorConfiguration {

  @Inject
  private DataSource dataSource;

  @Bean
  public HealthIndicator dbHealthIndicator() {
    return new DatabaseHealthIndicator(dataSource);
  }
}
