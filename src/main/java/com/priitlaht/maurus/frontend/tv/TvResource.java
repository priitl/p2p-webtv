package com.priitlaht.maurus.frontend.tv;

import com.codahale.metrics.annotation.Timed;
import com.priitlaht.maurus.backend.domain.UserShow;
import com.priitlaht.maurus.backend.service.TvService;
import com.priitlaht.maurus.common.util.security.SecurityUtil;
import com.priitlaht.maurus.frontend.common.util.HeaderUtil;
import com.priitlaht.maurus.frontend.common.util.PaginationUtil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/tv/", produces = MediaType.APPLICATION_JSON_VALUE)
public class TvResource {
  @Inject
  private TvService tvService;

  @Timed
  @RequestMapping(value = "popular", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<TvBasicDTO>> findPopularTv(Pageable pageable) throws URISyntaxException {
    log.debug("REST request to get a page of popular tv");
    Page<TvBasicDTO> page = tvService.findPopularTv(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tv/popular");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  @Timed
  @RequestMapping(value = "feed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<TvFeedDTO>> findUserTvFeed(Pageable pageable) throws URISyntaxException {
    log.debug("REST request to get a page of user's tv feed");
    Page<TvFeedDTO> page = tvService.findTvFeed(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tv/feed");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  @Timed
  @RequestMapping(value = "user-show", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> insertUserShow(@RequestBody UserShow userShow) {
    log.debug("REST request to insert UserShow: {}", userShow.getShowName());
    if (!Objects.equals(userShow.getUserLogin(), SecurityUtil.getCurrentUserLogin())) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tv", "invalid_user", "invalid user")).body(null);
    }
    tvService.createUserShow(userShow);
    return ResponseEntity.ok().headers(HeaderUtil.createAlert("tv.created", userShow.getShowName())).build();
  }

  @Timed
  @RequestMapping(value = "user-show", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> deleteUserShow(@RequestParam String userLogin, @RequestParam String showName) {
    log.debug("REST request to delete UserShow: {}", showName);
    if (!Objects.equals(userLogin, SecurityUtil.getCurrentUserLogin())) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tv", "invalid_user", "invalid user")).body(null);
    }
    tvService.deleteUserShow(userLogin, showName);
    return ResponseEntity.ok().headers(HeaderUtil.createAlert("tv.deleted", showName)).build();
  }

}
