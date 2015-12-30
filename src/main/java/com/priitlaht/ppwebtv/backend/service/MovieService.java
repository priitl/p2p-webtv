package com.priitlaht.ppwebtv.backend.service;

import com.priitlaht.ppwebtv.common.ApplicationProperties;
import com.priitlaht.ppwebtv.frontend.movie.CastDTO;
import com.priitlaht.ppwebtv.frontend.movie.MovieDetailsDTO;
import com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO;
import com.uwetrottmann.trakt.v2.TraktV2;
import com.uwetrottmann.trakt.v2.entities.CastMember;
import com.uwetrottmann.trakt.v2.entities.Credits;
import com.uwetrottmann.trakt.v2.entities.CrewMember;
import com.uwetrottmann.trakt.v2.entities.Movie;
import com.uwetrottmann.trakt.v2.entities.Person;
import com.uwetrottmann.trakt.v2.services.Movies;
import com.uwetrottmann.trakt.v2.services.People;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.priitlaht.ppwebtv.frontend.tv.MediaBasicDTO.createFromMovie;
import static com.uwetrottmann.trakt.v2.enums.Extended.FULLIMAGES;
import static com.uwetrottmann.trakt.v2.enums.Extended.IMAGES;

/**
 * @author Priit Laht
 */
@Service
@Transactional
public class MovieService {
  private static final int RESULT_LIMIT = 55000;
  private Movies moviesService;
  private People peopleService;

  @Inject
  private ApplicationProperties applicationProperties;

  @PostConstruct
  private void init() {
    TraktV2 traktV2 = new TraktV2();
    traktV2.setApiKey(applicationProperties.getTrakt().getApiKey());
    moviesService = traktV2.movies();
    peopleService = traktV2.people();
  }

  public Page<MediaBasicDTO> findPopularMovies(Pageable pageable) {
    List<MediaBasicDTO> result = new ArrayList<>();
    List<Movie> popularMovies = moviesService.popular(pageable.getPageNumber(), pageable.getPageSize(), IMAGES);
    popularMovies.stream().filter(m -> m.images != null && m.images.poster != null).forEach(m -> result.add(createFromMovie(m)));
    return new PageImpl<>(result, pageable, RESULT_LIMIT);
  }

  public Optional<MovieDetailsDTO> getMovieDetails(String imdbId) {
    Movie movie = moviesService.summary(imdbId, FULLIMAGES);
    Credits credits = moviesService.people(imdbId);
    return movie == null ? Optional.empty() : Optional.of(getMovieDetailsDTO(movie, credits));
  }

  private MovieDetailsDTO getMovieDetailsDTO(Movie movie, Credits credits) {
    MovieDetailsDTO result = new MovieDetailsDTO();
    BeanUtils.copyProperties(createFromMovie(movie), result);
    result.setOverview(movie.overview);
    result.setRuntime(movie.runtime);
    result.setReleaseDate(movie.released != null ? movie.released.toDate() : null);
    result.setTagline(movie.tagline);
    result.setLanguage(movie.language);
    result.setBackdropPath(movie.images.fanart.full);
    result.setGenre(String.join(" / ", movie.genres));
    result.setRating(movie.rating);
    result.setCast(createCastList(credits.cast));
    result.setDirector(formatCrewMembers(credits.crew.directing));
    result.setWriter(formatCrewMembers(credits.crew.writing));
    return result;
  }

  private String formatCrewMembers(List<CrewMember> crewMembers) {
    return String.join(" / ", crewMembers.stream().limit(3).map(crew -> crew.person.name).collect(Collectors.toList()));
  }

  private List<CastDTO> createCastList(List<CastMember> castMembers) {
    List<CastDTO> result = new ArrayList<>();
    castMembers.stream().filter(cast -> cast.person != null).forEach(cast -> result.add(getCastDTO(cast)));
    return result;
  }

  private CastDTO getCastDTO(CastMember cast) {
    CastDTO castDTO = new CastDTO();
    castDTO.setName(cast.person.name);
    castDTO.setCharacter(cast.character);
    return castDTO;
  }

}
