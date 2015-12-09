package com.priitlaht.maurus.frontend.tv;

import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.results.ResultList;
import com.priitlaht.maurus.backend.service.TvService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;


@RestController
@RequestMapping(value = "/api/tv", produces = MediaType.APPLICATION_JSON_VALUE)
public class TvResource {
  @Inject
  private TvService tvService;


  @RequestMapping(method = RequestMethod.GET)
  public ResultList<TVBasic> getPopularTv() {
    return tvService.getPopularTv();
  }

}
