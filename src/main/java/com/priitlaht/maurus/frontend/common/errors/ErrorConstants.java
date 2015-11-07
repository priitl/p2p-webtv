package com.priitlaht.maurus.frontend.common.errors;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ErrorConstants {
  public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
  public static final String ERR_ACCESS_DENIED = "error.accessDenied";
  public static final String ERR_VALIDATION = "error.validation";
  public static final String ERR_METHOD_NOT_SUPPORTED = "error.methodNotSupported";
}
