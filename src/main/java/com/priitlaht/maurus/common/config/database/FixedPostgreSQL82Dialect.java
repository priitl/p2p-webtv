package com.priitlaht.maurus.common.config.database;

import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import static java.sql.Types.BLOB;

public class FixedPostgreSQL82Dialect extends PostgreSQL82Dialect {

  public FixedPostgreSQL82Dialect() {
    super();
    registerColumnType(BLOB, "bytea");
  }

  @Override
  public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
    return sqlTypeDescriptor.getSqlType() == BLOB ? BinaryTypeDescriptor.INSTANCE : super.remapSqlTypeDescriptor(sqlTypeDescriptor);
  }
}
