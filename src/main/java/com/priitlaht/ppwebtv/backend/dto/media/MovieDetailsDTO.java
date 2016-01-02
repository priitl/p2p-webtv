package com.priitlaht.ppwebtv.backend.dto.media;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Priit Laht
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MovieDetailsDTO extends MediaBasicDTO {
  private String overview;
  private Integer runtime;
  private String tagline;
  private String status;
  private String originalLanguage;
  private String genre;
  private BigDecimal budget;
  private BigDecimal revenue;
  private BigDecimal rating;
  private String fullBackdropPath;
  private List<CastDTO> cast;
  private String director;
  private String writer;
  private List<MediaBasicDTO> similarMovies;

  @JsonIgnore
  public void addSimilarMovie(MediaBasicDTO movie) {
    if (similarMovies == null) {
      similarMovies = new ArrayList<>();
    }
    similarMovies.add(movie);
  }
}


