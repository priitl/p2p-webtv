package com.priitlaht.maurus.backend.service;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.maurus.common.ApplicationProperties;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author Priit Laht
 */
@Service
public class TvService {
  private TheMovieDbApi movieDbApi;

  @Inject
  private ApplicationProperties applicationProperties;

  @PostConstruct
  private void init() throws MovieDbException {
    movieDbApi = new TheMovieDbApi(applicationProperties.getMovieDatabase().getApiKey());
  }

  public Page<TVBasic> findPopularTv(Pageable pageable) {
    ResultList<TVBasic> result = new ResultList<>();
    try {
      result = movieDbApi.getTVPopular(pageable.getPageNumber(), null);
      List<TVBasic> emptyResults = new ArrayList<>();
      for (TVBasic tv : result.getResults()) {
        if (tv.getPosterPath() == null) {
          emptyResults.add(tv);
        }
        tv.setPosterPath(movieDbApi.createImageUrl(tv.getPosterPath(), "w342").toString());
      }
      result.getResults().removeAll(emptyResults);
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new PageImpl<>(result.getResults(), pageable, result.getTotalResults());
  }

}
