package com.priitlaht.maurus.frontend.tv;

import java.util.Date;

import lombok.Data;

/**
 * @author Priit Laht
 */
@Data
public class TvFeedDTO {
  private String showName;
  private String seasonNumber;
  private String episodeNumber;
  private String magnetUri;
  private Integer seeds;
  private Integer leeches;
  private Date uploadDate;
  private String posterPath;
}
