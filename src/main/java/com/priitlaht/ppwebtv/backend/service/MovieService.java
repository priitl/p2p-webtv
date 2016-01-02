package com.priitlaht.ppwebtv.backend.service;

import com.omertron.themoviedbapi.model.AbstractIdName;
import com.omertron.themoviedbapi.model.credits.MediaCredit;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.ppwebtv.backend.dto.media.MediaBasicDTO;
import com.priitlaht.ppwebtv.backend.dto.media.MovieDetailsDTO;

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

import javax.inject.Inject;

import static com.priitlaht.ppwebtv.backend.dto.media.CastDTO.createCastListFromMovie;
import static com.priitlaht.ppwebtv.backend.dto.media.MediaBasicDTO.createFromMovie;
import static java.lang.String.join;

/**
 * @author Priit Laht
 */
@Service
@Transactional
public class MovieService {
  @Inject
  private TmdbService tmdbService;

  public Page<MediaBasicDTO> findPopularMovies(Pageable pageable) {
    List<MediaBasicDTO> movieResult = new ArrayList<>();
    ResultList<MovieInfo> popularMovies = tmdbService.getPopularMovies(pageable.getPageNumber());
    popularMovies.getResults().stream().filter(movie -> movie.getPosterPath() != null).forEach(
      movie -> movieResult.add(createFromMovie(movie, tmdbService.getFullImageUrl(movie.getPosterPath(), "w342"))));
    return new PageImpl<>(movieResult, pageable, popularMovies.getTotalResults());
  }

  public Optional<MovieDetailsDTO> getMovieDetails(int tmdbId) {
    MovieInfo movieInfo = tmdbService.getMovieInfo(tmdbId, "credits", "similar");
    return Optional.of(getMovieDetailsDTO(movieInfo));
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
    result.setCast(createCastListFromMovie(movie, tmdbService));
    result.setDirector(getCrewByDepartment(movie, "directing"));
    result.setWriter(getCrewByDepartment(movie, "writing"));
    result.setFullBackdropPath(tmdbService.getFullImageUrl(movie.getBackdropPath(), "original"));
    movie.getSimilarMovies().stream().filter(sm -> sm.getPosterPath() != null).limit(6).forEach(
      sm -> result.addSimilarMovie(createFromMovie(sm, tmdbService.getFullImageUrl(sm.getPosterPath(), "w342"))));
    BeanUtils.copyProperties(createFromMovie(movie, tmdbService.getFullImageUrl(movie.getPosterPath(), "w342")), result);
    return result;
  }

  private String getCrewByDepartment(MovieInfo movie, String department) {
    return join(" / ", movie.getCrew().stream().filter(crew -> crew.getDepartment().equalsIgnoreCase(department))
      .limit(3).map(MediaCredit::getName).collect(Collectors.toList()));
  }

}
