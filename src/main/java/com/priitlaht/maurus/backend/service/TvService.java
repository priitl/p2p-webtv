package com.priitlaht.maurus.backend.service;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.maurus.backend.domain.UserShow;
import com.priitlaht.maurus.backend.repository.UserShowRepository;
import com.priitlaht.maurus.common.ApplicationProperties;
import com.priitlaht.maurus.common.util.security.SecurityUtil;
import com.priitlaht.maurus.frontend.tv.TvBasicDTO;
import com.priitlaht.maurus.frontend.tv.TvFeedDTO;
import com.priitlaht.strikeapi.StrikeApi;
import com.priitlaht.strikeapi.model.Category;
import com.priitlaht.strikeapi.model.Torrent;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Priit Laht
 */
@Service
@Transactional
public class TvService {
  private TheMovieDbApi movieDbApi;
  private Pattern torrentTitlePattern;

  @Inject
  private ApplicationProperties applicationProperties;
  @Inject
  private UserShowRepository userShowRepository;

  @PostConstruct
  private void init() throws MovieDbException {
    movieDbApi = new TheMovieDbApi(applicationProperties.getMovieDatabase().getApiKey());
    torrentTitlePattern = Pattern.compile("(?:S|s|season)(\\d{2})(?:E|e|X|x|episode|\n)(\\d{2})");
  }

  public Page<TvBasicDTO> findPopularTv(Pageable pageable) {
    List<TvBasicDTO> tvResult = new ArrayList<>();
    ResultList<TVBasic> popularTv = new ResultList<>();
    List<UserShow> userShows = userShowRepository.findAllByUserLogin(SecurityUtil.getCurrentUserLogin());
    try {
      popularTv = movieDbApi.getTVPopular(pageable.getPageNumber(), null);
      for (TVBasic tv : popularTv.getResults()) {
        if (tv.getPosterPath() != null) {
          tvResult.add(getTvBasicDTO(userShows, tv));
        } else {
          popularTv.setTotalResults(popularTv.getTotalResults() - 1);
        }
      }
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return new PageImpl<>(tvResult, pageable, popularTv.getTotalResults());
  }

  public Page<TvFeedDTO> findTvFeed(Pageable pageable) {
    List<TvFeedDTO> feedResult = new ArrayList<>();
    List<UserShow> userShows = userShowRepository.findAllByUserLogin(SecurityUtil.getCurrentUserLogin());
    for (UserShow userShow : userShows) {
      feedResult.addAll(findTvFeedByShow(userShow));
    }
    Comparator<TvFeedDTO> byUploadDate = (tv1, tv2) -> tv1.getUploadDate().compareTo(tv2.getUploadDate());
    Collections.sort(feedResult, byUploadDate.reversed());
    List<TvFeedDTO> page = new ArrayList<>();
    for (int i = (pageable.getPageNumber() - 1) * pageable.getPageSize(); i < feedResult.size() - 1 && i < (pageable.getPageNumber()) * pageable.getPageSize(); i++) {
      page.add(feedResult.get(i));
    }
    return new PageImpl<>(page, pageable, feedResult.size());
  }

  private List<TvFeedDTO> findTvFeedByShow(UserShow userShow) {
    List<TvFeedDTO> result = new ArrayList<>();
    List<Torrent> showTorrents = StrikeApi.search(userShow.getShowName());
    for (Torrent showTorrent : showTorrents) {
      Pair<String, String> seasonEpisodePair = getSeasonEpisodePair(showTorrent.getTitle());
      boolean isAlreadyAdded = result.stream().anyMatch(
        tv -> Objects.equals(tv.getSeasonNumber(), seasonEpisodePair.getLeft())
          && Objects.equals(tv.getEpisodeNumber(), seasonEpisodePair.getRight()));
      if (isNotBlank(seasonEpisodePair.getLeft()) && isNotBlank(seasonEpisodePair.getRight()) && !isAlreadyAdded) {
        TvFeedDTO feedDTO = new TvFeedDTO();
        feedDTO.setShowName(userShow.getShowName());
        feedDTO.setSeasonNumber(seasonEpisodePair.getLeft());
        feedDTO.setEpisodeNumber(seasonEpisodePair.getRight());
        feedDTO.setMagnetUri(showTorrent.getMagnetUri());
        feedDTO.setSeeds(showTorrent.getSeeds());
        feedDTO.setLeeches(showTorrent.getLeeches());
        feedDTO.setUploadDate(showTorrent.getUploadDate());
        feedDTO.setPosterPath(userShow.getPosterPath());
        result.add(feedDTO);
      }
    }
    return result;
  }

  private Pair<String, String> getSeasonEpisodePair(String torrentTitle) {
    Matcher m = torrentTitlePattern.matcher(torrentTitle);
    if (m.find()) {
      return Pair.of(m.group(1), m.group(2));
    }
    return Pair.of("", "");
  }

  private TvBasicDTO getTvBasicDTO(List<UserShow> userShows, TVBasic tv) throws MovieDbException {
    TvBasicDTO userTvBasic = new TvBasicDTO();
    BeanUtils.copyProperties(tv, userTvBasic);
    userTvBasic.setFullPosterPath(movieDbApi.createImageUrl(tv.getPosterPath(), "w342").toString());
    userTvBasic.setFavorite(userShows.stream().anyMatch(us -> Objects.equals(us.getShowName(), tv.getName())));
    return userTvBasic;
  }

  public UserShow createUserShow(UserShow userShow) {
    return userShowRepository.save(userShow);
  }

  public void deleteUserShow(String userLogin, String showName) {
    userShowRepository.deleteByUserLoginAndShowName(userLogin, showName);
  }

}
