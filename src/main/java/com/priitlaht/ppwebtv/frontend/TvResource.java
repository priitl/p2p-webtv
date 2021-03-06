package com.priitlaht.ppwebtv.frontend;

import com.codahale.metrics.annotation.Timed;
import com.priitlaht.ppwebtv.backend.domain.UserShow;
import com.priitlaht.ppwebtv.backend.dto.media.MediaBasicDTO;
import com.priitlaht.ppwebtv.backend.dto.media.SeasonDTO;
import com.priitlaht.ppwebtv.backend.dto.media.TvDetailsDTO;
import com.priitlaht.ppwebtv.backend.dto.media.TvFeedDTO;
import com.priitlaht.ppwebtv.backend.service.TvService;
import com.priitlaht.ppwebtv.common.util.security.SecurityUtil;
import com.priitlaht.ppwebtv.frontend.common.util.HeaderUtil;
import com.priitlaht.ppwebtv.frontend.common.util.PaginationUtil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<List<MediaBasicDTO>> findPopularTv(Pageable pageable) throws URISyntaxException {
    log.debug("REST request to get a page of popular tv");
    Page<MediaBasicDTO> page = tvService.findPopularTv(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tv/popular");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  @Timed
  @RequestMapping(value = "feed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<TvFeedDTO> findUserTvFeed(@RequestParam String apiUrl) throws URISyntaxException {
    log.debug("REST request to get user's tv feed");
    return tvService.findTvFeed(apiUrl);
  }

  @Timed
  @RequestMapping(value = "{tmdbId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TvDetailsDTO> getTvDetails(@PathVariable int tmdbId) throws URISyntaxException {
    log.debug("REST request get tv details: {}", tmdbId);
    return tvService.getTvDetails(tmdbId)
      .map(tv -> new ResponseEntity<>(tv, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Timed
  @RequestMapping(value = "{tmdbId}/season/{seasonNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SeasonDTO> getSeasonDetails(@PathVariable int tmdbId, @PathVariable int seasonNumber,
                                                    @RequestParam(required = false) String apiUrl) throws URISyntaxException {
    log.debug("REST request get season info: {} {}", tmdbId, seasonNumber);
    return tvService.findSeasonInfo(tmdbId, seasonNumber, apiUrl)
      .map(season -> new ResponseEntity<>(season, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @Timed
  @RequestMapping(value = "user-show", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> insertUserShow(@RequestBody UserShow userShow) {
    log.debug("REST request to insert UserShow: {}", userShow.getTitle());
    if (!Objects.equals(userShow.getUserLogin(), SecurityUtil.getCurrentUserLogin())) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tv", "invalid_user", "invalid user")).body(null);
    }
    return tvService.createUserShow(userShow)
      .map(user -> new ResponseEntity<String>(HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @Timed
  @RequestMapping(value = "user-show", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> deleteUserShow(@RequestParam String userLogin, @RequestParam Long tmdbId) {
    log.debug("REST request to delete UserShow: {}", tmdbId);
    if (!Objects.equals(userLogin, SecurityUtil.getCurrentUserLogin())) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tv", "invalid_user", "invalid user")).body(null);
    }
    tvService.deleteUserShow(userLogin, tmdbId);
    return ResponseEntity.ok().headers(HeaderUtil.createAlert("tv.deleted", tmdbId.toString())).build();
  }

}
