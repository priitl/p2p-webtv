package com.priitlaht.ppwebtv.frontend.common.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * Utility class for handling pagination.
 *
 * <p> Pagination uses the same principles as the <a href="https://developer.github.com/v3/#pagination">Github API</api>, and
 * follow <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link header)</a>. </p>
 */
@NoArgsConstructor(access = PRIVATE)
public final class PaginationUtil {

  public static HttpHeaders generatePaginationHttpHeaders(Page<?> page, String baseUrl) throws URISyntaxException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Total-Count", "" + page.getTotalElements());
    String safeBaseUrl = baseUrl;
    try {
      safeBaseUrl = URLEncoder.encode(baseUrl, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    String link = "";
    if ((page.getNumber() + 1) < page.getTotalPages()) {
      link = "<" + (new URI(safeBaseUrl + "?page=" + (page.getNumber() + 1) + "&size=" + page.getSize())).toString() + ">; rel=\"next\",";
    }
    if ((page.getNumber()) > 0) {
      link += "<" + (new URI(safeBaseUrl + "?page=" + (page.getNumber() - 1) + "&size=" + page.getSize())).toString() + ">; rel=\"prev\",";
    }
    int lastPage = 0;
    if (page.getTotalPages() > 0) {
      lastPage = page.getTotalPages() - 1;
    }
    link += "<" + (new URI(safeBaseUrl + "?page=" + lastPage + "&size=" + page.getSize())).toString() + ">; rel=\"last\",";
    link += "<" + (new URI(safeBaseUrl + "?page=" + 0 + "&size=" + page.getSize())).toString() + ">; rel=\"first\"";
    headers.add(HttpHeaders.LINK, link);
    return headers;
  }
}
