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
public class TvDetailsDTO extends MediaBasicDTO {
  private String overview;
  private String runtime;
  private String status;
  private String originalLanguage;
  private String genre;
  private BigDecimal rating;
  private String fullBackdropPath;
  private List<CastDTO> cast;
  private String director;
  private String writer;
  private Integer numberOfSeasons;
  private List<MediaBasicDTO> similarShows;

  @JsonIgnore
  public void addSimilarTv(MediaBasicDTO tv) {
    if (similarShows == null) {
      similarShows = new ArrayList<>();
    }
    similarShows.add(tv);
  }
}
