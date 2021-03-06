package com.priitlaht.ppwebtv.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * <p> Properties are configured in the application.yml file. </p>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "p2p-webtv", ignoreUnknownFields = false)
public class ApplicationProperties {
  private final Async async = new Async();
  private final Http http = new Http();
  private final Datasource datasource = new Datasource();
  private final Cache cache = new Cache();
  private final Mail mail = new Mail();
  private final Security security = new Security();
  private final Swagger swagger = new Swagger();
  private final MovieDatabase movieDatabase = new MovieDatabase();
  private final Eztv eztv = new Eztv();
  private final Metrics metrics = new Metrics();
  private final CorsConfiguration cors = new CorsConfiguration();
  private final Social social = new Social();

  @Getter
  @Setter
  public static class Async {
    private int corePoolSize = 2;
    private int maxPoolSize = 50;
    private int queueCapacity = 10000;
  }

  @Getter
  @Setter
  public static class Http {
    private final Cache cache = new Cache();

    @Getter
    @Setter
    public static class Cache {
      private int timeToLiveInDays = 31;
    }
  }

  @Getter
  @Setter
  public static class Datasource {
    private boolean cachePrepStmts = true;
    private int prepStmtCacheSize = 250;
    private int prepStmtCacheSqlLimit = 2048;
    private boolean useServerPrepStmts = true;
  }

  @Getter
  @Setter
  public static class Cache {
    private final Ehcache ehcache = new Ehcache();
    private int timeToLiveSeconds = 3600;

    @Getter
    @Setter
    public static class Ehcache {
      private String maxBytesLocalHeap = "16M";
    }
  }

  @Getter
  @Setter
  public static class Mail {
    private String from = "ppwebtv@localhost";
  }

  @Getter
  @Setter
  public static class Security {
    private final Rememberme rememberme = new Rememberme();

    @Getter
    @Setter
    public static class Rememberme {

      @NotNull
      private String key;
    }
  }

  @Getter
  @Setter
  public static class Swagger {
    private String title = "ppwebtv API";
    private String description = "ppwebtv API documentation";
    private String version = "0.0.1";
    private String termsOfServiceUrl;
    private String contact;
    private String license;
    private String licenseUrl;
  }

  @Getter
  @Setter
  public static class MovieDatabase {
    private String apiKey;
  }

  @Getter
  @Setter
  public static class Eztv {
    private String apiUrl;
  }

  @Getter
  @Setter
  public static class Metrics {
    private final Jmx jmx = new Jmx();
    private final Spark spark = new Spark();
    private final Graphite graphite = new Graphite();

    @Getter
    @Setter
    public static class Jmx {
      private boolean enabled = true;
    }

    @Getter
    @Setter
    public static class Spark {
      private boolean enabled = false;
      private String host = "localhost";
      private int port = 9999;
    }

    @Getter
    @Setter
    public static class Graphite {
      private boolean enabled = false;
      private String host = "localhost";
      private int port = 2003;
      private String prefix = "ppwebtv";
    }
  }

  @Getter
  @Setter
  public static class Social {
    private String redirectAfterSignIn = "/#/home";
  }
}
