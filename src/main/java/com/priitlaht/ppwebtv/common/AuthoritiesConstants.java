package com.priitlaht.ppwebtv.common;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * ApplicationConstants for Spring Security authorities.
 */
@NoArgsConstructor(access = PRIVATE)
public final class AuthoritiesConstants {
  public static final String ADMIN = "ROLE_ADMIN";
  public static final String USER = "ROLE_USER";
  public static final String ANONYMOUS = "ROLE_ANONYMOUS";
}
