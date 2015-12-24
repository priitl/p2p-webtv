package com.priitlaht.ppwebtv.frontend.movie;

import lombok.Data;

/**
 * @author Priit Laht
 */
@Data
public class MoviePopularDTO {
  private String title;
  private int tmdbId;
  private String releaseDateString;
  private String fullPosterPath;
}
