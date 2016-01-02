package com.priitlaht.ppwebtv.backend.dto.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omertron.themoviedbapi.model.credits.MediaCreditCast;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.model.tv.TVInfo;
import com.priitlaht.ppwebtv.backend.service.TmdbService;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author Priit Laht
 */
@Data
public class CastDTO {
  private String name;
  private String character;
  private String fullArtworkPath;

  @JsonIgnore
  public static List<CastDTO> createCastListFromMovie(MovieInfo movie, TmdbService tmdbService) {
    List<CastDTO> result = new ArrayList<>();
    movie.getCast().stream().filter(cast -> cast.getArtworkPath() != null).forEach(cast -> result.add(getCastDTO(cast, tmdbService)));
    return result;
  }

  @JsonIgnore
  public static List<CastDTO> createCastListFromTv(TVInfo tvInfo, TmdbService tmdbService) {
    List<CastDTO> result = new ArrayList<>();
    tvInfo.getCredits().getCast().stream().filter(cast -> cast.getArtworkPath() != null).forEach(cast -> result.add(getCastDTO(cast, tmdbService)));
    return result;
  }

  @JsonIgnore
  private static CastDTO getCastDTO(MediaCreditCast cast, TmdbService tmdbService) {
    CastDTO castDTO = new CastDTO();
    castDTO.setName(cast.getName());
    castDTO.setCharacter(cast.getCharacter());
    castDTO.setFullArtworkPath(tmdbService.getFullImageUrl(cast.getArtworkPath(), "h632"));
    return castDTO;
  }
}
