package com.priitlaht.ppwebtv.frontend.common.filter;

import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter is used in production, to put HTTP cache headers with a long (1 month) expiration time.
 */
public class CachingHttpHeadersFilter implements Filter {
  private final static long LAST_MODIFIED = System.currentTimeMillis();
  private long CACHE_TIME_TO_LIVE = TimeUnit.DAYS.toMillis(31L);

  private Environment environment;

  public CachingHttpHeadersFilter(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    CACHE_TIME_TO_LIVE = TimeUnit.DAYS.toMillis(environment.getProperty("p2p-webtv.http.cache.timeToLiveInDays", Long.class, 31L));
  }

  @Override
  public void destroy() {
    // Nothing to destroy
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    httpResponse.setHeader("Cache-Control", "max-age=" + CACHE_TIME_TO_LIVE + ", public");
    httpResponse.setHeader("Pragma", "cache");

    // Setting Expires header, for proxy caching
    httpResponse.setDateHeader("Expires", CACHE_TIME_TO_LIVE + System.currentTimeMillis());

    // Setting the Last-Modified header, for browser caching
    httpResponse.setDateHeader("Last-Modified", LAST_MODIFIED);

    chain.doFilter(request, response);
  }
}
