package com.priitlaht.ppwebtv.frontend.tv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.priitlaht.ppwebtv.backend.domain.UserShow;
import com.uwetrottmann.trakt.v2.entities.Movie;
import com.uwetrottmann.trakt.v2.entities.Show;

import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Priit Laht
 */
@Getter
@Setter
public class MediaBasicDTO {
  private String imdbId;
  private String title;
  private Integer year;
  private String posterPath;
  private boolean favorite;
  private boolean movie;

  @JsonIgnore
  public static MediaBasicDTO createFromShow(List<UserShow> userShows, Show show) {
    MediaBasicDTO result = new MediaBasicDTO();
    result.setTitle(show.title);
    result.setImdbId(show.ids.imdb);
    result.setYear(show.year);
    result.setFavorite(userShows.stream().anyMatch(us -> Objects.equals(us.getImdbId(), show.ids.imdb)));
    result.setPosterPath(show.images.poster.medium);
    return result;
  }

  @JsonIgnore
  public static MediaBasicDTO createFromMovie(Movie movie) {
    MediaBasicDTO result = new MediaBasicDTO();
    result.setTitle(movie.title);
    result.setImdbId(movie.ids.imdb);
    result.setYear(movie.year);
    result.setPosterPath(movie.images.poster.medium);
    result.setMovie(true);
    return result;
  }
}
