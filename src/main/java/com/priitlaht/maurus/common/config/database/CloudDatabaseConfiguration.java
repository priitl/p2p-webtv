package com.priitlaht.maurus.common.config.database;

import com.priitlaht.maurus.common.ApplicationConstants;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile(ApplicationConstants.SPRING_PROFILE_CLOUD)
public class CloudDatabaseConfiguration extends AbstractCloudConfig {

  @Bean
  public DataSource dataSource() {
    log.info("Configuring JDBC datasource from a cloud provider");
    return connectionFactory().dataSource();
  }
}
