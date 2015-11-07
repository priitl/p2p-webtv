package com.priitlaht.maurus;

import com.priitlaht.maurus.common.ApplicationConstants;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationInitializer extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.profiles(addDefaultProfile()).sources(Application.class);
  }

  private String addDefaultProfile() {
    String profile = System.getProperty("spring.profiles.active");
    if (profile != null) {
      log.info("Running with Spring profile(s) : {}", profile);
      return profile;
    }
    log.warn("No Spring profile configured, running with default configuration");
    return ApplicationConstants.SPRING_PROFILE_DEVELOPMENT;
  }
}
