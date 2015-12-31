package com.priitlaht.ppwebtv.backend.service;

import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.ppwebtv.backend.domain.UserShow;
import com.priitlaht.ppwebtv.backend.repository.UserShowRepository;
import com.priitlaht.ppwebtv.common.util.security.SecurityUtil;
import com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO.createFromMovie;
import static com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO.createFromTv;

/**
 * @author Priit Laht
 */
@Service
@Transactional
public class MediaSearchService {
  private static final int RESULT_LIMIT = 100;
  @Inject
  private UserShowRepository userShowRepository;
  @Inject
  private TmdbService tmdbService;

  public Page<MediaBasicDTO> search(String title, Pageable pageable) {
    List<MediaBasicDTO> result = new ArrayList<>();
    result.addAll(getTvResults(title, pageable));
    result.addAll(getMovieResults(title, pageable));
    return new PageImpl<>(result, pageable, RESULT_LIMIT);
  }

  public List<MediaBasicDTO> getTvResults(String title, Pageable pageable) {
    List<MediaBasicDTO> tvResult = new ArrayList<>();
    List<UserShow> userShows = userShowRepository.findAllByUserLogin(SecurityUtil.getCurrentUserLogin());
    ResultList<TVBasic> searchResult = tmdbService.searchTV(title, pageable.getPageNumber());
    searchResult.getResults().stream().filter(tv -> tv.getPosterPath() != null).forEach(
      tv -> tvResult.add(createFromTv(userShows, tv, tmdbService.getFullImageUrl(tv.getPosterPath(), "w342"))));
    return tvResult;
  }

  public List<MediaBasicDTO> getMovieResults(String title, Pageable pageable) {
    List<MediaBasicDTO> movieResult = new ArrayList<>();
    ResultList<MovieInfo> searchResult = tmdbService.searchMovie(title, pageable.getPageNumber());
    searchResult.getResults().stream().filter(movie -> movie.getPosterPath() != null).forEach(
      movie -> movieResult.add(createFromMovie(movie, tmdbService.getFullImageUrl(movie.getPosterPath(), "w342"))));
    return movieResult;
  }
}
