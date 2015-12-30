package com.priitlaht.ppwebtv.backend.service;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.ppwebtv.backend.domain.UserShow;
import com.priitlaht.ppwebtv.backend.repository.UserShowRepository;
import com.priitlaht.ppwebtv.common.ApplicationProperties;
import com.priitlaht.ppwebtv.common.util.security.SecurityUtil;
import com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO.createFromMovie;
import static com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO.createFromTv;

/**
 * @author Priit Laht
 */
@Service
@Transactional
public class MediaSearchService {
  private static final int RESULT_LIMIT = 100;
  private TheMovieDbApi movieDbApi;

  @Inject
  private ApplicationProperties applicationProperties;
  @Inject
  private UserShowRepository userShowRepository;

  @PostConstruct
  private void init() throws MovieDbException {
    movieDbApi = new TheMovieDbApi(applicationProperties.getMovieDatabase().getApiKey());
  }

  public Page<MediaBasicDTO> search(String title, Pageable pageable) {
    List<MediaBasicDTO> result = new ArrayList<>();
    result.addAll(getTvResults(title, pageable));
    result.addAll(getMovieResults(title, pageable));
    return new PageImpl<>(result, pageable, RESULT_LIMIT);
  }

  public List<MediaBasicDTO> getTvResults(String title, Pageable pageable) {
    List<MediaBasicDTO> tvResult = new ArrayList<>();
    List<UserShow> userShows = userShowRepository.findAllByUserLogin(SecurityUtil.getCurrentUserLogin());
    try {
      ResultList<TVBasic> searchResult = movieDbApi.searchTV(title, pageable.getPageNumber(), null, null, null);
      searchResult.getResults().stream().filter(tv -> tv.getPosterPath() != null).forEach(tv -> tvResult.add(createFromTv(userShows, tv, movieDbApi)));
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return tvResult;
  }

  public List<MediaBasicDTO> getMovieResults(String title, Pageable pageable) {
    List<MediaBasicDTO> movieResult = new ArrayList<>();
    try {
      ResultList<MovieInfo> searchResult = movieDbApi.searchMovie(title, pageable.getPageNumber(), null, true, null, null, null);
      searchResult.getResults().stream().filter(movie -> movie.getPosterPath() != null).forEach(movie -> movieResult.add(createFromMovie(movie, movieDbApi)));
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return movieResult;
  }
}
