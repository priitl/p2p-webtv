package com.priitlaht.ppwebtv.backend.service;

import com.omertron.themoviedbapi.model.AbstractIdName;
import com.omertron.themoviedbapi.model.credits.MediaCredit;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo;
import com.omertron.themoviedbapi.model.tv.TVInfo;
import com.omertron.themoviedbapi.model.tv.TVSeasonInfo;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.eztvapi.EztvApi;
import com.priitlaht.eztvapi.model.Episode;
import com.priitlaht.eztvapi.model.Torrents;
import com.priitlaht.eztvapi.model.TvShowDetails;
import com.priitlaht.ppwebtv.backend.domain.UserShow;
import com.priitlaht.ppwebtv.backend.dto.media.MediaBasicDTO;
import com.priitlaht.ppwebtv.backend.dto.media.SeasonDTO;
import com.priitlaht.ppwebtv.backend.dto.media.TvDetailsDTO;
import com.priitlaht.ppwebtv.backend.dto.media.TvFeedDTO;
import com.priitlaht.ppwebtv.backend.repository.UserShowRepository;
import com.priitlaht.ppwebtv.common.util.security.SecurityUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;

import static com.priitlaht.ppwebtv.backend.dto.media.CastDTO.createCastListFromTv;
import static com.priitlaht.ppwebtv.backend.dto.media.MediaBasicDTO.createFromTv;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Priit Laht
 */
@Service
@Transactional
public class TvService {
  @Inject
  private UserShowRepository userShowRepository;
  @Inject
  private TmdbService tmdbService;

  public Page<MediaBasicDTO> findPopularTv(Pageable pageable) {
    List<MediaBasicDTO> tvResult = new ArrayList<>();
    List<UserShow> userShows = userShowRepository.findAllByUserLogin(SecurityUtil.getCurrentUserLogin());
    ResultList<TVBasic> popularTv = tmdbService.getPopularTV(pageable.getPageNumber());
    popularTv.getResults().stream().filter(tv -> tv.getPosterPath() != null).forEach(
      tv -> tvResult.add(createFromTv(userShows, tv, tmdbService.getFullImageUrl(tv.getPosterPath(), "w342"))));
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

  public Optional<TvDetailsDTO> getTvDetails(int tmdbId) {
    List<UserShow> userShows = userShowRepository.findAllByUserLogin(SecurityUtil.getCurrentUserLogin());
    TVInfo tvInfo = tmdbService.getTVInfo(tmdbId, "credits", "similar");
    return Optional.of(getTvDetailsDTO(tvInfo, userShows));
  }

  private TvDetailsDTO getTvDetailsDTO(TVInfo tvInfo, List<UserShow> userShows) {
    TvDetailsDTO result = new TvDetailsDTO();
    result.setOverview(tvInfo.getOverview());
    result.setStatus(tvInfo.getStatus());
    result.setRuntime(join(" / ", tvInfo.getEpisodeRunTime().stream().limit(3).map(Object::toString).collect(Collectors.toList())));
    result.setGenre(join(" / ", tvInfo.getGenres().stream().limit(3).map(AbstractIdName::getName).collect(Collectors.toList())));
    result.setOriginalLanguage(tvInfo.getOriginalLanguage());
    result.setRating(BigDecimal.valueOf(tvInfo.getVoteAverage()));
    result.setCast(createCastListFromTv(tvInfo, tmdbService));
    result.setDirector(getCrewByDepartment(tvInfo, "directing"));
    result.setWriter(getCrewByDepartment(tvInfo, "writing"));
    result.setFullBackdropPath(tmdbService.getFullImageUrl(tvInfo.getBackdropPath(), "original"));
    result.setNumberOfSeasons(tvInfo.getNumberOfSeasons());
    tvInfo.getSimilarTV().stream().filter(sm -> sm.getPosterPath() != null).limit(6).forEach(
      sm -> result.addSimilarTv(createFromTv(userShows, sm, tmdbService.getFullImageUrl(sm.getPosterPath(), "w342"))));
    BeanUtils.copyProperties(createFromTv(userShows, tvInfo, tmdbService.getFullImageUrl(tvInfo.getPosterPath(), "w342")), result);
    return result;
  }

  private List<TvFeedDTO> findTvFeedByShow(UserShow userShow, String apiUrl) {
    List<TvFeedDTO> result = new ArrayList<>();
    TvShowDetails tvShow = EztvApi.getTvShowDetails(apiUrl, userShow.getImdbId());
    if (tvShow != null) {
      tvShow.getEpisodes().forEach(e -> result.add(createFeedDTO(tvShow.getTitle(), e)));
    }
    return result;
  }

  private String getCrewByDepartment(TVInfo tvInfo, String department) {
    return join(" / ", tvInfo.getCredits().getCrew().stream().filter(crew -> crew.getDepartment().equalsIgnoreCase(department))
      .limit(3).map(MediaCredit::getName).collect(Collectors.toList()));
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
    feedDTO.setAirDate(episode.getFirstAirDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
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

  public Optional<UserShow> createUserShow(UserShow userShow) {
    TVInfo tvInfo = tmdbService.getTVInfo(userShow.getTmdbId().intValue(), "external_ids");
    userShow.setImdbId(tvInfo.getExternalIDs().getImdbId());
    return Optional.of(userShowRepository.save(userShow));
  }

  public void deleteUserShow(String userLogin, Long tmdbId) {
    userShowRepository.deleteByUserLoginAndTmdbId(userLogin, tmdbId);
  }

  public Optional<SeasonDTO> findSeasonInfo(int tmdbId, int seasonNumber, String apiUrl) {
    List<Episode> torrentEpisodes = new ArrayList<>();
    if (isNotBlank(apiUrl)) {
      TVInfo tvInfo = tmdbService.getTVInfo(tmdbId, "external_ids");
      TvShowDetails torrentDetails = EztvApi.getTvShowDetails(apiUrl, tvInfo.getExternalIDs().getImdbId());
      if (torrentDetails != null) {
        torrentEpisodes.addAll(torrentDetails.getEpisodes());
      }
    }
    TVSeasonInfo tvSeasonInfo = tmdbService.getTvSeasonInfo(tmdbId, seasonNumber);
    return Optional.of(SeasonDTO.createFromSeasonInfo(tvSeasonInfo, getEpisodes(tvSeasonInfo.getEpisodes(), torrentEpisodes)));
  }

  private List<TvFeedDTO> getEpisodes(List<TVEpisodeInfo> episodes, List<Episode> torrentEpisodes) {
    List<TvFeedDTO> result = new ArrayList<>();
    for (TVEpisodeInfo episode : episodes) {
      Optional<Episode> torrentEpisode = torrentEpisodes.stream().filter(
        te -> te.getEpisodeNumber() == episode.getEpisodeNumber() && te.getSeasonNumber() == episode.getSeasonNumber()).findFirst();
      result.add(torrentEpisode.isPresent() ? createFeedDTO(null, torrentEpisode.get()) : createFeedDTO(episode));
    }
    return result;
  }

  private TvFeedDTO createFeedDTO(TVEpisodeInfo episode) {
    TvFeedDTO feedDTO = new TvFeedDTO();
    feedDTO.setEpisodeTitle(episode.getName());
    feedDTO.setOverview(episode.getOverview());
    feedDTO.setSeasonNumber(String.format("%02d", episode.getSeasonNumber()));
    feedDTO.setEpisodeNumber(String.format("%02d", episode.getEpisodeNumber()));
    feedDTO.setAirDate(episode.getAirDate() != null ? LocalDate.parse(episode.getAirDate()) : null);
    return feedDTO;
  }
}
