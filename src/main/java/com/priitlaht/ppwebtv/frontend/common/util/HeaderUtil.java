package com.priitlaht.ppwebtv.frontend.common.util;

import org.springframework.http.HttpHeaders;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * Utility class for http header creation.
 */
@NoArgsConstructor(access = PRIVATE)
public final class HeaderUtil {

  public static HttpHeaders createAlert(String message, String param) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-wtvApp-alert", message);
    headers.add("X-wtvApp-params", param);
    return headers;
  }

  public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
    return createAlert("wtvApp." + entityName + ".created", param);
  }

  public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
    return createAlert("wtvApp." + entityName + ".updated", param);
  }

  public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
    return createAlert("wtvApp." + entityName + ".deleted", param);
  }

  public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-wtvApp-error", "error." + errorKey);
    headers.add("X-wtvApp-params", entityName);
    return headers;
  }
}
