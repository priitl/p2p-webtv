package com.priitlaht.maurus.common.config.cache;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ehcache.InstrumentedEhcache;
import com.priitlaht.maurus.common.ApplicationConstants;
import com.priitlaht.maurus.common.ApplicationProperties;
import com.priitlaht.maurus.common.config.database.DatabaseConfiguration;
import com.priitlaht.maurus.common.config.metrics.MetricsConfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.SortedSet;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableCaching
@AutoConfigureAfter(value = {MetricsConfiguration.class, DatabaseConfiguration.class})
@Profile("!" + ApplicationConstants.SPRING_PROFILE_FAST)
public class CacheConfiguration {

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private MetricRegistry metricRegistry;

  private net.sf.ehcache.CacheManager cacheManager;

  @PreDestroy
  public void destroy() {
    log.info("Remove Cache Manager metrics");
    SortedSet<String> names = metricRegistry.getNames();
    names.forEach(metricRegistry::remove);
    log.info("Closing Cache Manager");
    cacheManager.shutdown();
  }

  @Bean
  public CacheManager cacheManager(ApplicationProperties applicationProperties) {
    log.debug("Starting Ehcache");
    cacheManager = net.sf.ehcache.CacheManager.create();
    cacheManager.getConfiguration().setMaxBytesLocalHeap(applicationProperties.getCache().getEhcache().getMaxBytesLocalHeap());
    log.debug("Registering Ehcache Metrics gauges");
    Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
    for (EntityType<?> entity : entities) {
      String name = entity.getName();
      if (name == null || entity.getJavaType() != null) {
        name = entity.getJavaType().getName();
      }
      Assert.notNull(name, "entity cannot exist without a identifier");

      net.sf.ehcache.Cache cache = cacheManager.getCache(name);
      if (cache != null) {
        cache.getCacheConfiguration().setTimeToLiveSeconds(applicationProperties.getCache().getTimeToLiveSeconds());
        net.sf.ehcache.Ehcache decoratedCache = InstrumentedEhcache.instrument(metricRegistry, cache);
        cacheManager.replaceCacheWithDecoratedCache(cache, decoratedCache);
      }
    }
    EhCacheCacheManager ehCacheManager = new EhCacheCacheManager();
    ehCacheManager.setCacheManager(cacheManager);
    return ehCacheManager;
  }
}
