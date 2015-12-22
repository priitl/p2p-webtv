package com.priitlaht.ppwebtv.backend.service;

import com.omertron.omdbapi.OmdbApi;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.ppwebtv.common.ApplicationProperties;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

  public Page<MovieInfo> findPopularMovies(Pageable pageable) {
    List<MovieInfo> movieResult = new ArrayList<>();
    ResultList<MovieInfo> popularMovies = new ResultList<>();
    try {
      popularMovies = movieDbApi.getPopularMovieList(pageable.getPageNumber(), null);
      popularMovies.getResults().stream().filter(movie -> movie.getPosterPath() != null).forEach(movie -> movieResult.add(updatePosterUrl(movie)));
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new PageImpl<>(movieResult, pageable, popularMovies.getTotalResults());
  }

  private MovieInfo updatePosterUrl(MovieInfo movie) {
    try {
      movie.setPosterPath(movieDbApi.createImageUrl(movie.getPosterPath(), "w342").toString());
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return movie;
  }


}
