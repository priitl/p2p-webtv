package com.priitlaht.ppwebtv.frontend.movie;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Priit Laht
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MovieDetailsDTO extends MoviePopularDTO {
  private String overview;
  private String fullBackdropPath;
}
