package com.priitlaht.ppwebtv.common.config.database;

import org.hibernate.dialect.H2Dialect;

import static java.sql.Types.FLOAT;

public class FixedH2Dialect extends H2Dialect {

  public FixedH2Dialect() {
    super();
    registerColumnType(FLOAT, "real");
  }
}
