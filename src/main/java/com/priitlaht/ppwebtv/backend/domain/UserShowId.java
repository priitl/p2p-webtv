package com.priitlaht.ppwebtv.backend.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Priit Laht
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserShowId implements Serializable {
  private String userLogin;
  private String imdbId;
}
