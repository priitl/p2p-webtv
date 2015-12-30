package com.priitlaht.ppwebtv.backend.service;

import com.priitlaht.ppwebtv.backend.domain.UserShow;
import com.priitlaht.ppwebtv.backend.repository.UserShowRepository;
import com.priitlaht.ppwebtv.common.ApplicationProperties;
import com.priitlaht.ppwebtv.common.util.security.SecurityUtil;
import com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO;
import com.uwetrottmann.trakt.v2.TraktV2;
import com.uwetrottmann.trakt.v2.entities.Movie;
import com.uwetrottmann.trakt.v2.entities.SearchResult;
import com.uwetrottmann.trakt.v2.entities.Show;
import com.uwetrottmann.trakt.v2.enums.Type;
import com.uwetrottmann.trakt.v2.services.Search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO.createFromMovie;
import static com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO.createFromShow;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

/**
 * @author Priit Laht
 */
@Service
@Transactional
public class MediaSearchService {
  private static final int RESULT_LIMIT = 100;
  private Search searchService;

  @Inject
  private ApplicationProperties applicationProperties;
  @Inject
  private UserShowRepository userShowRepository;

  @PostConstruct
  private void init() {
    TraktV2 traktV2 = new TraktV2();
    traktV2.setApiKey(applicationProperties.getTrakt().getApiKey());
    searchService = traktV2.search();
  }

  public Page<MediaBasicDTO> search(String title, Pageable pageable) {
    List<MediaBasicDTO> result = new ArrayList<>();
    result.addAll(getTvResults(title, pageable));
    result.addAll(getMovieResults(title, pageable));
    Comparator<MediaBasicDTO> byDate = (mb1, mb2) -> mb1.getYear().compareTo(mb2.getYear());
    Collections.sort(result, byDate.reversed());
    return new PageImpl<>(result, pageable, RESULT_LIMIT);
  }

  private List<MediaBasicDTO> getTvResults(String title, Pageable pageable) {
    List<MediaBasicDTO> result = new ArrayList<>();
    List<SearchResult> tvResults = searchService.textQuery(title, Type.SHOW, null, pageable.getPageNumber(), pageable.getPageSize() / 2);
    List<UserShow> userShows = userShowRepository.findAllByUserLogin(SecurityUtil.getCurrentUserLogin());
    if (isNotEmpty(tvResults)) {
      List<Show> shows = tvResults.stream().map(tv -> tv.show).collect(Collectors.toList());
      if (isNotEmpty(shows)) {
        shows.stream().filter(tv -> tv.images != null && isNotBlank(tv.images.poster.medium) && tv.year != null
          && tv.title.toLowerCase().contains(title.toLowerCase())).forEach(tv -> result.add(createFromShow(userShows, tv)));
      }
    }
    return result;
  }

  private List<MediaBasicDTO> getMovieResults(String title, Pageable pageable) {
    List<MediaBasicDTO> result = new ArrayList<>();
    List<SearchResult> movieResults = searchService.textQuery(title, Type.MOVIE, null, pageable.getPageNumber(), pageable.getPageSize() / 2);
    if (isNotEmpty(movieResults)) {
      List<Movie> movies = movieResults.stream().map(m -> m.movie).collect(Collectors.toList());
      if (isNotEmpty(movies)) {
        movies.stream().filter(m -> m.images != null && isNotBlank(m.images.poster.medium) && m.year != null
          && m.title.toLowerCase().contains(title.toLowerCase())).forEach(m -> result.add(createFromMovie(m)));
      }
    }
    return result;
  }
}
