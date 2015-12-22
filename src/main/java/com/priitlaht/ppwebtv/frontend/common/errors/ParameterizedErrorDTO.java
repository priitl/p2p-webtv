package com.priitlaht.ppwebtv.frontend.common.errors;

import java.io.Serializable;

import lombok.Data;

/**
 * DTO for sending a parameterized error message.
 */
@Data
public class ParameterizedErrorDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String message;
  private final String[] params;

  public ParameterizedErrorDTO(String message, String... params) {
    this.message = message;
    this.params = params;
  }

}
