package com.priitlaht.maurus;

import com.priitlaht.maurus.common.ApplicationProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import static com.priitlaht.maurus.common.ApplicationConstants.SPRING_PROFILE_CLOUD;
import static com.priitlaht.maurus.common.ApplicationConstants.SPRING_PROFILE_DEVELOPMENT;
import static com.priitlaht.maurus.common.ApplicationConstants.SPRING_PROFILE_FAST;
import static com.priitlaht.maurus.common.ApplicationConstants.SPRING_PROFILE_PRODUCTION;

@Slf4j
@ComponentScan
@EnableAutoConfiguration(exclude = {MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class})
@EnableConfigurationProperties({ApplicationProperties.class, LiquibaseProperties.class})
public class Application {

  @Inject
  private Environment environment;

  public static void main(String[] args) throws UnknownHostException {
    SpringApplication app = new SpringApplication(Application.class);
    SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
    addDefaultProfile(app, source);
    Environment env = app.run(args).getEnvironment();
    String divider = "----------------------------------------------------------";
    log.info("Access URLs:\n{}\n\tLocal: \t\thttp://127.0.0.1:{}\n\tExternal: \thttp://{}:{}\n{}",
      divider, env.getProperty("server.port"), InetAddress.getLocalHost().getHostAddress(), env.getProperty("server.port"), divider);
  }

  @PostConstruct
  public void initApplication() throws IOException {
    if (environment.getActiveProfiles().length == 0) {
      log.warn("No Spring profile configured, running with default configuration");
    } else {
      log.info("Running with Spring profile(s) : {}", Arrays.toString(environment.getActiveProfiles()));
      Collection<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
      if (activeProfiles.contains(SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(SPRING_PROFILE_PRODUCTION)) {
        log.error("You have misconfigured your application! It should not run with both the 'dev' and 'prod' profiles at the same time.");
      }
      if (activeProfiles.contains(SPRING_PROFILE_PRODUCTION) && activeProfiles.contains(SPRING_PROFILE_FAST)) {
        log.error("You have misconfigured your application! It should not run with both the 'prod' and 'fast' profiles at the same time.");
      }
      if (activeProfiles.contains(SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(SPRING_PROFILE_CLOUD)) {
        log.error("You have misconfigured your application! It should not run with both the 'dev' and 'cloud' profiles at the same time.");
      }
    }
  }

  private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
    if (!source.containsProperty("spring.profiles.active") && !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {
      app.setAdditionalProfiles(SPRING_PROFILE_DEVELOPMENT);
    }
  }
}
