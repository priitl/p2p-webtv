package com.priitlaht.maurus.frontend.common.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.NoArgsConstructor;

import static java.lang.String.format;
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
    String link = "";
    if ((page.getNumber() + 1) < page.getTotalPages()) {
      link = "<" + (new URI(format("%s?page=%d&size=%d", baseUrl, page.getNumber() + 1, page.getSize()))).toString() + ">; rel=\"next\",";
    }
    // prev link
    if ((page.getNumber()) > 0) {
      link += "<" + (new URI(format("%s?page=%d&size=%d", baseUrl, page.getNumber() - 1, page.getSize()))).toString() + ">; rel=\"prev\",";
    }
    // last and first link
    link += "<" + (new URI(format("%s?page=%d&size=%d", baseUrl, page.getTotalPages() - 1, page.getSize()))).toString() + ">; rel=\"last\",";
    link += "<" + (new URI(format("%s?page=%d&size=%d", baseUrl, 0, page.getSize()))).toString() + ">; rel=\"first\"";
    headers.add(HttpHeaders.LINK, link);
    return headers;
  }
}
