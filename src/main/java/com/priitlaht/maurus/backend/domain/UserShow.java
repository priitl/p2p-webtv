package com.priitlaht.maurus.backend.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@IdClass(UserShowId.class)
@Table(name = "user_show")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserShow implements Serializable {

  @Id
  @Column(name = "user_login", length = 100, nullable = false)
  private String userLogin;

  @Id
  @Column(name = "tmdb_id", length = 10, nullable = false)
  private Long tmdbId;

  @Column(name = "imdb_id", length = 10, nullable = false)
  private String imdbId;

  @Column(name = "title", length = 255, nullable = false)
  private String title;

}
