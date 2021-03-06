package com.priitlaht.ppwebtv.common;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ApplicationConstants {
  public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
  public static final String SPRING_PROFILE_PRODUCTION = "prod";
  public static final String SPRING_PROFILE_FAST = "fast";
  public static final String SPRING_PROFILE_CLOUD = "cloud";
  public static final String SPRING_PROFILE_HEROKU = "heroku";
  public static final String SYSTEM_ACCOUNT = "system";
  public static final String IP_ADDRESS = "IP_ADDRESS";
}
