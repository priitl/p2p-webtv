package com.priitlaht.ppwebtv.backend.service;

import com.omertron.omdbapi.OMDBException;
import com.omertron.omdbapi.OmdbApi;
import com.omertron.omdbapi.model.OmdbVideoBasic;
import com.omertron.omdbapi.tools.OmdbBuilder;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.eztvapi.EztvApi;
import com.priitlaht.eztvapi.model.Episode;
import com.priitlaht.eztvapi.model.Torrents;
import com.priitlaht.eztvapi.model.TvShowDetails;
import com.priitlaht.ppwebtv.backend.domain.UserShow;
import com.priitlaht.ppwebtv.backend.repository.UserShowRepository;
import com.priitlaht.ppwebtv.common.ApplicationProperties;
import com.priitlaht.ppwebtv.common.util.security.SecurityUtil;
import com.priitlaht.ppwebtv.frontend.tv.TvFeedDTO;
import com.priitlaht.ppwebtv.frontend.tv.TvPopularDTO;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author Priit Laht
 */
@Service
@Transactional
public class TvService {
  private TheMovieDbApi movieDbApi;
  private OmdbApi omdbApi;

  @Inject
  private ApplicationProperties applicationProperties;
  @Inject
  private UserShowRepository userShowRepository;

  @PostConstruct
  private void init() throws MovieDbException {
    movieDbApi = new TheMovieDbApi(applicationProperties.getMovieDatabase().getApiKey());
    omdbApi = new OmdbApi();
  }

  public Page<TvPopularDTO> findPopularTv(Pageable pageable) {
    List<TvPopularDTO> tvResult = new ArrayList<>();
    ResultList<TVBasic> popularTv = new ResultList<>();
    List<UserShow> userShows = userShowRepository.findAllByUserLogin(SecurityUtil.getCurrentUserLogin());
    try {
      popularTv = movieDbApi.getTVPopular(pageable.getPageNumber(), null);
      popularTv.getResults().stream().filter(tv -> tv.getPosterPath() != null).forEach(tv -> tvResult.add(getTvBasicDTO(userShows, tv)));
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new PageImpl<>(tvResult, pageable, popularTv.getTotalResults());
  }

  public List<TvFeedDTO> findTvFeed(String apiUrl) {
    List<TvFeedDTO> feedResult = new ArrayList<>();
    List<UserShow> userShows = userShowRepository.findAllByUserLogin(SecurityUtil.getCurrentUserLogin());
    userShows.parallelStream().forEach(show -> feedResult.addAll(findTvFeedByShow(show, apiUrl)));
    Comparator<TvFeedDTO> byUploadDate = (tv1, tv2) -> tv1.getAirDate().compareTo(tv2.getAirDate());
    Comparator<TvFeedDTO> byEpisodeNumber = (tv1, tv2) -> tv1.getEpisodeNumber().compareTo(tv2.getEpisodeNumber());
    Collections.sort(feedResult, byUploadDate.thenComparing(byEpisodeNumber).reversed());
    return feedResult;
  }

  public Page<TvPopularDTO> searchTv(String title, Pageable pageable) {
    List<TvPopularDTO> tvResult = new ArrayList<>();
    ResultList<TVBasic> searchResult = new ResultList<>();
    List<UserShow> userShows = userShowRepository.findAllByUserLogin(SecurityUtil.getCurrentUserLogin());
    try {
      searchResult = movieDbApi.searchTV(title, pageable.getPageNumber(), null, null, null);
      searchResult.getResults().stream().filter(tv -> tv.getPosterPath() != null).forEach(tv -> tvResult.add(getTvBasicDTO(userShows, tv)));
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new PageImpl<>(tvResult, pageable, searchResult.getTotalResults());
  }

  private List<TvFeedDTO> findTvFeedByShow(UserShow userShow, String apiUrl) {
    List<TvFeedDTO> result = new ArrayList<>();
    TvShowDetails tvShow = EztvApi.getTvShowDetails(apiUrl, userShow.getImdbId());
    if (tvShow != null) {
      tvShow.getEpisodes().forEach(e -> result.add(createFeedDTO(tvShow.getTitle(), e)));
    }
    return result;
  }

  private TvFeedDTO createFeedDTO(String showTitle, Episode episode) {
    TvFeedDTO feedDTO = new TvFeedDTO();
    feedDTO.setShowTitle(showTitle);
    feedDTO.setTvdbId(episode.getTvdbId());
    feedDTO.setEpisodeTitle(episode.getTitle());
    feedDTO.setOverview(episode.getOverview());
    feedDTO.setSeasonNumber(String.format("%02d", episode.getSeasonNumber()));
    feedDTO.setEpisodeNumber(String.format("%02d", episode.getEpisodeNumber()));
    feedDTO.setMagnetUri(getTorrentMagnetUri(episode.getTorrents()));
    feedDTO.setAirDate(episode.getFirstAirDate());
    Random rand = new Random();
    feedDTO.setSeeds(rand.nextInt((1000 - 50) + 1) + 50);
    feedDTO.setLeeches(rand.nextInt((2000 - 100) + 1) + 100);
    return feedDTO;
  }

  private String getTorrentMagnetUri(Torrents torrents) {
    if (torrents.getHd720p() != null) {
      return torrents.getHd720p().getMagnetUri();
    } else if (torrents.getSd480p() != null) {
      return torrents.getSd480p().getMagnetUri();
    } else if (torrents.getUnknown() != null) {
      return torrents.getUnknown().getMagnetUri();
    } else {
      return StringUtils.EMPTY;
    }
  }

  private TvPopularDTO getTvBasicDTO(List<UserShow> userShows, TVBasic tv) {
    TvPopularDTO result = new TvPopularDTO();
    result.setTitle(tv.getName().replaceAll("Marvel's ", ""));
    result.setTmdbId(tv.getId());
    result.setReleaseDateString(tv.getFirstAirDate());
    result.setFavorite(userShows.stream().anyMatch(us -> us.getTmdbId() == tv.getId()));
    try {
      result.setFullPosterPath(movieDbApi.createImageUrl(tv.getPosterPath(), "w342").toString());
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return result;
  }

  public Optional<UserShow> createUserShow(UserShow userShow) {
    try {
      OmdbVideoBasic search = omdbApi.getInfo(new OmdbBuilder().setTitle(userShow.getTitle()).setTypeSeries().build());
      userShow.setImdbId(search.getImdbID());
      return Optional.of(userShowRepository.save(userShow));
    } catch (OMDBException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  public void deleteUserShow(String userLogin, Long tmdbId) {
    userShowRepository.deleteByUserLoginAndTmdbId(userLogin, tmdbId);
  }

}
