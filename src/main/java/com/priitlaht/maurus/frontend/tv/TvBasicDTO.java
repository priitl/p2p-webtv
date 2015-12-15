package com.priitlaht.maurus.frontend.tv;

import com.omertron.themoviedbapi.model.tv.TVBasic;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
