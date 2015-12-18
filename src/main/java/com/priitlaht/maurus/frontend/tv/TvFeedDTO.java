package com.priitlaht.maurus.frontend.tv;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Priit Laht
 */
@Getter
@Setter
public class TvFeedDTO {
  private String showTitle;
  private String episodeTitle;
  private String seasonNumber;
  private String episodeNumber;
  private String overview;
  private String magnetUri;
  private String tvdbId;
  private Integer seeds;
  private Integer leeches;
  private Date airDate;
}
