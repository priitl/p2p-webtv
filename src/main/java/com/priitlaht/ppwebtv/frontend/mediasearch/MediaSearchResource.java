package com.priitlaht.ppwebtv.frontend.mediasearch;

import com.codahale.metrics.annotation.Timed;
import com.priitlaht.ppwebtv.backend.service.MediaSearchService;
import com.priitlaht.ppwebtv.frontend.common.util.PaginationUtil;
import com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Priit Laht
 */
@Slf4j
@RestController
@RequestMapping(value = "api/media/search/", produces = MediaType.APPLICATION_JSON_VALUE)
public class MediaSearchResource {
  @Inject
  private MediaSearchService mediaSearchService;

  @Timed
  @RequestMapping(value = "{title}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MediaBasicDTO>> search(@PathVariable String title, Pageable pageable) throws URISyntaxException {
    log.debug("REST request to search media by title: {}", title);
    Page<MediaBasicDTO> page = mediaSearchService.search(title, pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/media/search/" + title);
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }
}
