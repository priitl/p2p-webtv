package com.priitlaht.ppwebtv.common.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlets.MetricsServlet;
import com.priitlaht.ppwebtv.common.ApplicationConstants;
import com.priitlaht.ppwebtv.common.ApplicationProperties;
import com.priitlaht.ppwebtv.common.config.cache.CacheConfiguration;
import com.priitlaht.ppwebtv.frontend.common.filter.CachingHttpHeadersFilter;
import com.priitlaht.ppwebtv.frontend.common.filter.StaticResourcesProductionFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.EnumSet;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Slf4j
@Configuration
@AutoConfigureAfter(CacheConfiguration.class)
public class WebConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

  @Inject
  private Environment environment;
  @Inject
  private ApplicationProperties applicationProperties;

  @Autowired(required = false)
  private MetricRegistry metricRegistry;

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    log.info("Web application configuration, using profiles: {}", Arrays.toString(environment.getActiveProfiles()));
    EnumSet<DispatcherType> dispatchers = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
    if (!environment.acceptsProfiles(ApplicationConstants.SPRING_PROFILE_FAST)) {
      initMetrics(servletContext, dispatchers);
    }
    if (environment.acceptsProfiles(ApplicationConstants.SPRING_PROFILE_PRODUCTION)) {
      initCachingHttpHeadersFilter(servletContext, dispatchers);
      initStaticResourcesProductionFilter(servletContext, dispatchers);
    }
    log.info("Web application fully configured");
  }

  /**
   * Set up Mime types.
   */
  @Override
  public void customize(ConfigurableEmbeddedServletContainer container) {
    MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
    mappings.add("html", "text/html;charset=utf-8");
    mappings.add("json", "text/html;charset=utf-8");
    container.setMimeMappings(mappings);
  }

  /**
   * Initializes the static resources production Filter.
   */
  private void initStaticResourcesProductionFilter(ServletContext servletContext, EnumSet<DispatcherType> dispatchers) {
    log.debug("Registering static resources production Filter");
    FilterRegistration.Dynamic staticResourcesProductionFilter =
      servletContext.addFilter("staticResourcesProductionFilter", new StaticResourcesProductionFilter());

    staticResourcesProductionFilter.addMappingForUrlPatterns(dispatchers, true, "/");
    staticResourcesProductionFilter.addMappingForUrlPatterns(dispatchers, true, "/index.html");
    staticResourcesProductionFilter.addMappingForUrlPatterns(dispatchers, true, "/assets/*");
    staticResourcesProductionFilter.addMappingForUrlPatterns(dispatchers, true, "/scripts/*");
    staticResourcesProductionFilter.setAsyncSupported(true);
  }

  /**
   * Initializes the caching HTTP Headers Filter.
   */
  private void initCachingHttpHeadersFilter(ServletContext servletContext, EnumSet<DispatcherType> dispatchers) {
    log.debug("Registering Caching HTTP Headers Filter");
    FilterRegistration.Dynamic cachingHttpHeadersFilter =
      servletContext.addFilter("cachingHttpHeadersFilter", new CachingHttpHeadersFilter(environment));

    cachingHttpHeadersFilter.addMappingForUrlPatterns(dispatchers, true, "/dist/assets/*");
    cachingHttpHeadersFilter.addMappingForUrlPatterns(dispatchers, true, "/dist/scripts/*");
    cachingHttpHeadersFilter.setAsyncSupported(true);
  }

  /**
   * Initializes Metrics.
   */
  private void initMetrics(ServletContext servletContext, EnumSet<DispatcherType> dispatchers) {
    log.debug("Initializing Metrics registries");
    servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, metricRegistry);
    servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);

    log.debug("Registering Metrics Filter");
    FilterRegistration.Dynamic metricsFilter = servletContext.addFilter("webappMetricsFilter", new InstrumentedFilter());
    metricsFilter.addMappingForUrlPatterns(dispatchers, true, "/*");
    metricsFilter.setAsyncSupported(true);

    log.debug("Registering Metrics Servlet");
    ServletRegistration.Dynamic metricsAdminServlet = servletContext.addServlet("metricsServlet", new MetricsServlet());
    metricsAdminServlet.addMapping("/metrics/metrics/*");
    metricsAdminServlet.setAsyncSupported(true);
    metricsAdminServlet.setLoadOnStartup(2);
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = applicationProperties.getCors();
    if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
      source.registerCorsConfiguration("/api/**", config);
      source.registerCorsConfiguration("/v2/api-docs", config);
      source.registerCorsConfiguration("/oauth/**", config);
    }
    return new CorsFilter(source);
  }
}
