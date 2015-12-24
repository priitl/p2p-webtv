package com.priitlaht.ppwebtv.frontend.tv;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Priit Laht
 */
@Getter
@Setter
public class TvPopularDTO {
  private String title;
  private int tmdbId;
  private String releaseDateString;
  private String fullPosterPath;
  private boolean favorite;
}
