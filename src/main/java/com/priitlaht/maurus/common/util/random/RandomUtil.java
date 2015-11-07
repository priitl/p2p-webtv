package com.priitlaht.maurus.common.util.random;

import org.apache.commons.lang.RandomStringUtils;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class RandomUtil {
  private static final int DEF_COUNT = 20;

  public static String generatePassword() {
    return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
  }

  public static String generateActivationKey() {
    return RandomStringUtils.randomNumeric(DEF_COUNT);
  }

  public static String generateResetKey() {
    return RandomStringUtils.randomNumeric(DEF_COUNT);
  }
}
