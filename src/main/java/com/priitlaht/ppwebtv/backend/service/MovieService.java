package com.priitlaht.ppwebtv.backend.service;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.AbstractIdName;
import com.omertron.themoviedbapi.model.credits.MediaCredit;
import com.omertron.themoviedbapi.model.credits.MediaCreditCast;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.ppwebtv.common.ApplicationProperties;
import com.priitlaht.ppwebtv.frontend.movie.CastDTO;
import com.priitlaht.ppwebtv.frontend.movie.MovieDetailsDTO;
import com.priitlaht.ppwebtv.frontend.movie.MoviePopularDTO;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static java.lang.String.join;

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
      MovieInfo movieInfo = movieDbApi.getMovieInfo(tmdbId, null, "credits", "similar");
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
    result.setBudget(BigDecimal.valueOf(movie.getBudget()));
    result.setRevenue(BigDecimal.valueOf(movie.getRevenue()));
    result.setRuntime(movie.getRuntime());
    result.setStatus(movie.getStatus());
    result.setTagline(movie.getTagline());
    result.setGenre(join(" / ", movie.getGenres().stream().limit(3).map(AbstractIdName::getName).collect(Collectors.toList())));
    result.setOriginalLanguage(movie.getOriginalLanguage());
    result.setRating(BigDecimal.valueOf(movie.getVoteAverage()));
    result.setCast(createCastList(movie));
    result.setDirector(getCrewByDepartment(movie, "directing"));
    result.setWriter(getCrewByDepartment(movie, "writing"));
    movie.getSimilarMovies().stream().filter(sm -> sm.getPosterPath() != null).limit(6).forEach(sm -> result.addSimilarMovie(getMoviePopularDTO(sm)));
    BeanUtils.copyProperties(getMoviePopularDTO(movie), result);
    try {
      result.setFullBackdropPath(movieDbApi.createImageUrl(movie.getBackdropPath(), "original").toString());
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return result;
  }

  private String getCrewByDepartment(MovieInfo movie, String department) {
    return join(" / ", movie.getCrew().stream().filter(crew -> crew.getDepartment().equalsIgnoreCase(department))
      .limit(3).map(MediaCredit::getName).collect(Collectors.toList()));
  }

  private List<CastDTO> createCastList(MovieInfo movie) {
    List<CastDTO> result = new ArrayList<>();
    movie.getCast().stream().filter(cast -> cast.getArtworkPath() != null).forEach(cast -> result.add(getCastDTO(cast)));
    return result;
  }

  private CastDTO getCastDTO(MediaCreditCast cast) {
    CastDTO castDTO = new CastDTO();
    castDTO.setName(cast.getName());
    castDTO.setCharacter(cast.getCharacter());
    try {
      castDTO.setFullArtworkPath(movieDbApi.createImageUrl(cast.getArtworkPath(), "h632").toString());
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return castDTO;
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
