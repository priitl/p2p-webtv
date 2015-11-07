package com.priitlaht.maurus.frontend.logs;

import ch.qos.logback.classic.Logger;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoggerDTO {
  private String name;
  private String level;

  public LoggerDTO(Logger logger) {
    this.name = logger.getName();
    this.level = logger.getEffectiveLevel().toString();
  }
}
