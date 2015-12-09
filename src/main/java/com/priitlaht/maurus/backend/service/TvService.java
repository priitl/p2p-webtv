package com.priitlaht.maurus.backend.service;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.maurus.common.ApplicationProperties;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Priit Laht
 */
@Slf4j
@Service
public class TvService {

  private TheMovieDbApi movieDbApi;

  @Inject
  private ApplicationProperties applicationProperties;

  @PostConstruct
  private void init() throws MovieDbException {
    movieDbApi = new TheMovieDbApi(applicationProperties.getMovieDatabase().getApiKey());
  }

  // TODO replace with infinite scroll
  public ResultList<TVBasic> getPopularTv() {
    ResultList<TVBasic> result = new ResultList<>();
    try {
      result = movieDbApi.getTVPopular(1, null);
      result.getResults().addAll(movieDbApi.getTVPopular(2, null).getResults());
      for (TVBasic tv : result.getResults()) {
        tv.setPosterPath(movieDbApi.createImageUrl(tv.getPosterPath(), "w342").toString());
      }
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return result;
  }

}
