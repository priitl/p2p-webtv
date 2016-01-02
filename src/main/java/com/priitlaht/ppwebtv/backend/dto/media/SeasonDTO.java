package com.priitlaht.ppwebtv.backend.dto.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omertron.themoviedbapi.model.tv.TVSeasonInfo;

import java.util.List;

import lombok.Data;

/**
 * @author Priit Laht
 */
@Data
public class SeasonDTO {
  private String name;
  private String overview;
  private List<TvFeedDTO> episodes;

  @JsonIgnore
  public static SeasonDTO createFromSeasonInfo(TVSeasonInfo seasonInfo, List<TvFeedDTO> episodes) {
    SeasonDTO result = new SeasonDTO();
    result.setName(seasonInfo.getName());
    result.setOverview(seasonInfo.getOverview());
    result.setEpisodes(episodes);
    return result;
  }

}
