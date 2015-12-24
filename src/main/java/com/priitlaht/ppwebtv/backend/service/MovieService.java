package com.priitlaht.ppwebtv.backend.service;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.ppwebtv.common.ApplicationProperties;
import com.priitlaht.ppwebtv.frontend.movie.MovieDetailsDTO;
import com.priitlaht.ppwebtv.frontend.movie.MoviePopularDTO;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author Priit Laht
 */
@Service
@Transactional
public class MovieService {
  private TheMovieDbApi movieDbApi;

  @Inject
  private ApplicationProperties applicationProperties;

  @PostConstruct
  private void init() throws MovieDbException {
    movieDbApi = new TheMovieDbApi(applicationProperties.getMovieDatabase().getApiKey());
  }

  public Page<MoviePopularDTO> findPopularMovies(Pageable pageable) {
    List<MoviePopularDTO> movieResult = new ArrayList<>();
    ResultList<MovieInfo> popularMovies = new ResultList<>();
    try {
      popularMovies = movieDbApi.getPopularMovieList(pageable.getPageNumber(), null);
      popularMovies.getResults().stream().filter(movie -> movie.getPosterPath() != null).forEach(movie -> movieResult.add(getMoviePopularDTO(movie)));
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new PageImpl<>(movieResult, pageable, popularMovies.getTotalResults());
  }

  public Optional<MovieDetailsDTO> getMovieDetails(int tmdbId) {
    try {
      MovieInfo movieInfo = movieDbApi.getMovieInfo(tmdbId, null);
      return movieInfo == null ? Optional.empty() : Optional.of(getMovieDetailsDTO(movieInfo));
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  public Page<MoviePopularDTO> searchMovies(String title, Pageable pageable) {
    List<MoviePopularDTO> movieResult = new ArrayList<>();
    ResultList<MovieInfo> searchResult = new ResultList<>();
    try {
      searchResult = movieDbApi.searchMovie(title, pageable.getPageNumber(), null, true, null, null, null);
      searchResult.getResults().stream().filter(movie -> movie.getPosterPath() != null).forEach(movie -> movieResult.add(getMoviePopularDTO(movie)));
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new PageImpl<>(movieResult, pageable, searchResult.getTotalResults());
  }

  private MovieDetailsDTO getMovieDetailsDTO(MovieInfo movie) {
    MovieDetailsDTO result = new MovieDetailsDTO();
    result.setOverview(movie.getOverview());
    BeanUtils.copyProperties(getMoviePopularDTO(movie), result);
    try {
      result.setFullBackdropPath(movieDbApi.createImageUrl(movie.getBackdropPath(), "original").toString());
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return result;
  }

  private MoviePopularDTO getMoviePopularDTO(MovieInfo movie) {
    MoviePopularDTO result = new MoviePopularDTO();
    result.setTitle(movie.getTitle());
    result.setTmdbId(movie.getId());
    result.setReleaseDateString(movie.getReleaseDate());
    try {
      result.setFullPosterPath(movieDbApi.createImageUrl(movie.getPosterPath(), "w342").toString());
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return result;
  }
}
