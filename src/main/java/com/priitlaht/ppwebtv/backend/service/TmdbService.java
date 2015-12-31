package com.priitlaht.ppwebtv.backend.service;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.model.tv.TVInfo;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.ppwebtv.common.ApplicationProperties;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author Priit Laht
 */
@Service
public class TmdbService {
  private TheMovieDbApi movieDbApi;

  @Inject
  private ApplicationProperties applicationProperties;

  @PostConstruct
  private void init() throws MovieDbException {
    movieDbApi = new TheMovieDbApi(applicationProperties.getMovieDatabase().getApiKey());
  }

  public String getFullImageUrl(String posterPath, String size) {
    try {
      return movieDbApi.createImageUrl(posterPath, size).toString();
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return "";
  }

  public ResultList<TVBasic> getPopularTV(int pageNumber) {
    try {
      return movieDbApi.getTVPopular(pageNumber, null);
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new ResultList<>();
  }

  public ResultList<TVBasic> searchTV(String title, int pageNumber) {
    try {
      return movieDbApi.searchTV(title, pageNumber, null, null, null);
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new ResultList<>();
  }

  public TVInfo getTVInfo(int tmdbId, String... appendToResponse) {
    try {
      return movieDbApi.getTVInfo(tmdbId, null, appendToResponse);
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new TVInfo();
  }

  public ResultList<MovieInfo> getPopularMovies(int pageNumber) {
    try {
      return movieDbApi.getPopularMovieList(pageNumber, null);
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new ResultList<>();
  }

  public ResultList<MovieInfo> searchMovie(String title, int pageNumber) {
    try {
      return movieDbApi.searchMovie(title, pageNumber, null, true, null, null, null);
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new ResultList<>();
  }

  public MovieInfo getMovieInfo(int tmdbId, String... appendToResponse) {
    try {
      return movieDbApi.getMovieInfo(tmdbId, null, appendToResponse);
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new MovieInfo();
  }
}
