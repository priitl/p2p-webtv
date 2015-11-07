package com.priitlaht.maurus.frontend.common.errors;

import java.io.Serializable;

import lombok.Data;

@Data
public class FieldErrorDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String objectName;
  private final String field;
  private final String message;

  protected FieldErrorDTO(String dto, String field, String message) {
    this.objectName = dto;
    this.field = field;
    this.message = message;
  }

}
