package com.priitlaht.ppwebtv.frontend.tv;

import com.omertron.themoviedbapi.model.tv.TVBasic;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Priit Laht
 */
@Getter
@Setter
public class TvBasicDTO extends TVBasic {
  private boolean favorite;
  private String fullPosterPath;
}
