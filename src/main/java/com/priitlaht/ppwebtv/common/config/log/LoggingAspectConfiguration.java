package com.priitlaht.ppwebtv.common.config.log;

import com.priitlaht.ppwebtv.common.ApplicationConstants;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

  @Bean
  @Profile(ApplicationConstants.SPRING_PROFILE_DEVELOPMENT)
  public LoggingAspect loggingAspect() {
    return new LoggingAspect();
  }
}
