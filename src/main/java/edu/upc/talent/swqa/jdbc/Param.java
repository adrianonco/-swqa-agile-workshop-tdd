package edu.upc.talent.swqa.jdbc;

import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;

@FunctionalInterface
public interface Param {
  void set(final PreparedStatement ps, final int i) throws SQLException;

  static Param p(final Integer value) {
    return (ps, i) -> ps.setObject(i, value, Types.INTEGER);
  }

  static Param p(final int value) {
    return (ps, i) -> ps.setInt(i, value);
  }

  static Param p(final String value) {
    return (ps, i) -> ps.setString(i, value);
  }

  static Param p(final Boolean value) {
    return (ps, i) -> ps.setObject(i, value, Types.BOOLEAN);
  }

  static Param p(final boolean value) {
    return (ps, i) -> ps.setBoolean(i, value);
  }

  static Param p(final Double value) {
    return (ps, i) -> ps.setObject(i, value, Types.DOUBLE);
  }

  static Param p(final double value) {
    return (ps, i) -> ps.setDouble(i, value);
  }

  static Param p(final Float value) {
    return (ps, i) -> ps.setObject(i, value, Types.FLOAT);
  }

  static Param p(final float value) {
    return (ps, i) -> ps.setFloat(i, value);
  }

  static Param p(final Long value) {
    return (ps, i) -> ps.setObject(i, value, Types.BIGINT);
  }

  static Param p(final long value) {
    return (ps, i) -> ps.setLong(i, value);
  }

  static Param p(final URI value) {
    return (ps, i) -> {
      if(value != null) ps.setString(i, value.toString());
      else ps.setObject(i, null, Types.VARCHAR);
    };
  }

  static Param p(final Instant value) {
    return (ps, i) -> {
      if(value != null) ps.setTimestamp(i, java.sql.Timestamp.from(value));
      else ps.setObject(i, null, Types.TIMESTAMP);
    };
  }

}
