package com.priitlaht.maurus.config.apidoc;

import com.priitlaht.maurus.config.ApplicationProperties;
import com.priitlaht.maurus.config.Constants;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * SpringFox Swagger configuration. <p> Warning! When having a lot of REST endpoints, SpringFox can become a performance issue. In that case, you can
 * use a specific Spring profile for this class, so that only front-end developers have access to the Swagger view.
 */
@Slf4j
@Configuration
@EnableSwagger2
@Profile("!" + Constants.SPRING_PROFILE_PRODUCTION)
public class SwaggerConfiguration {
  private static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

  @Bean
  public Docket swaggerSpringfoxDocket(ApplicationProperties applicationProperties) {
    log.debug("Starting Swagger");
    StopWatch watch = new StopWatch();
    watch.start();
    Docket docket = getDocket(applicationProperties);
    watch.stop();
    log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
    return docket;
  }

  private Docket getDocket(ApplicationProperties applicationProperties) {
    return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(getApiInfo(applicationProperties))
      .genericModelSubstitutes(ResponseEntity.class)
      .forCodeGeneration(true)
      .genericModelSubstitutes(ResponseEntity.class)
      .directModelSubstitute(java.time.LocalDate.class, String.class)
      .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
      .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
      .select()
      .paths(regex(DEFAULT_INCLUDE_PATTERN))
      .build();
  }

  private ApiInfo getApiInfo(ApplicationProperties applicationProperties) {
    return new ApiInfo(
      applicationProperties.getSwagger().getTitle(),
      applicationProperties.getSwagger().getDescription(),
      applicationProperties.getSwagger().getVersion(),
      applicationProperties.getSwagger().getTermsOfServiceUrl(),
      applicationProperties.getSwagger().getContact(),
      applicationProperties.getSwagger().getLicense(),
      applicationProperties.getSwagger().getLicenseUrl());
  }
}
