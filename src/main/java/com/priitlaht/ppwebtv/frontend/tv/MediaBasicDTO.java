package com.priitlaht.ppwebtv.frontend.tv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.priitlaht.ppwebtv.backend.domain.UserShow;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Priit Laht
 */
@Getter
@Setter
public class MediaBasicDTO {
  private String title;
  private int tmdbId;
  private String releaseDateString;
  private String fullPosterPath;
  private boolean favorite;
  private boolean movie;

  @JsonIgnore
  public static MediaBasicDTO createFromTv(List<UserShow> userShows, TVBasic tv, TheMovieDbApi movieDbApi) {
    MediaBasicDTO result = new MediaBasicDTO();
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

  @JsonIgnore
  public static MediaBasicDTO createFromMovie(MovieInfo movie, TheMovieDbApi movieDbApi) {
    MediaBasicDTO result = new MediaBasicDTO();
    result.setTitle(movie.getTitle());
    result.setTmdbId(movie.getId());
    result.setReleaseDateString(movie.getReleaseDate());
    result.setMovie(true);
    try {
      result.setFullPosterPath(movieDbApi.createImageUrl(movie.getPosterPath(), "w342").toString());
    } catch (MovieDbException e) {
      e.printStackTrace();
    }
    return result;
  }
}
