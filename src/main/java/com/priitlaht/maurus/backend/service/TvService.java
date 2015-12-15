package com.priitlaht.maurus.backend.service;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.maurus.backend.domain.UserShow;
import com.priitlaht.maurus.backend.repository.UserShowRepository;
import com.priitlaht.maurus.common.ApplicationProperties;
import com.priitlaht.maurus.common.util.security.SecurityUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Priit Laht
 */
@Service
@Transactional
public class TvService {
  private TheMovieDbApi movieDbApi;

  @Inject
  private ApplicationProperties applicationProperties;
  @Inject
  private UserShowRepository userShowRepository;

  @PostConstruct
  private void init() throws MovieDbException {
    movieDbApi = new TheMovieDbApi(applicationProperties.getMovieDatabase().getApiKey());
  }

  public Page<UserTvBasic> findPopularTv(Pageable pageable) {
    ResultList<TVBasic> popularTv = new ResultList<>();
    List<UserTvBasic> userTv = new ArrayList<>();
    List<UserShow> userShows = userShowRepository.findAllByUserLogin(SecurityUtil.getCurrentUserLogin());
    try {
      popularTv = movieDbApi.getTVPopular(pageable.getPageNumber(), null);
      for (TVBasic tv : popularTv.getResults()) {
        if (tv.getPosterPath() != null) {
          UserTvBasic userTvBasic = new UserTvBasic();
          BeanUtils.copyProperties(tv, userTvBasic);
          userTvBasic.setFullPosterPath(movieDbApi.createImageUrl(tv.getPosterPath(), "w342").toString());
          userTvBasic.setFavorite(userShows.stream().anyMatch(us -> Objects.equals(us.getShowName(), tv.getName())));
          userTv.add(userTvBasic);
        } else {
          popularTv.setTotalResults(popularTv.getTotalResults() - 1);
        }
      }
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new PageImpl<>(userTv, pageable, popularTv.getTotalResults());
  }

  public UserShow createUserShow(UserShow userShow) {
    return userShowRepository.save(userShow);
  }

  public void deleteUserShow(String userLogin, String showName) {
    userShowRepository.deleteByUserLoginAndShowName(userLogin, showName);
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public class UserTvBasic extends TVBasic {
    private boolean favorite;
    private String fullPosterPath;
  }

}
