package edu.upc.talent.swqa.jdbc;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public final class ResultSetView {

  private final ResultSet resultSet;

  public ResultSetView(final ResultSet resultSet) {
    this.resultSet = resultSet;
  }

  public Boolean getBoolean(final int column) throws SQLException {
    final var res = resultSet.getBoolean(column);
    return resultSet.wasNull() ? null : res;
  }

  public boolean getBool(final int column) throws SQLException {
    final var res = resultSet.getBoolean(column);
    if (resultSet.wasNull()) throw new SQLException("Column " + column + " is null");
    return res;
  }

  public String getString(final int column) throws SQLException {
    return resultSet.getString(column);
  }

  public Integer getInteger(final int column) throws SQLException {
    final var res = resultSet.getInt(column);
    return resultSet.wasNull() ? null : res;
  }

  public int getInt(final int column) throws SQLException {
    final var res = resultSet.getInt(column);
    if (resultSet.wasNull()) throw new SQLException("Column " + column + " is null");
    return res;
  }

  public Long getLong(final int column) throws SQLException {
    final var res = resultSet.getLong(column);
    return resultSet.wasNull() ? null : res;
  }

  public long getLongPrimitive(final int column) throws SQLException {
    final var res = resultSet.getLong(column);
    if (resultSet.wasNull()) throw new SQLException("Column " + column + " is null");
    return res;
  }

  public Double getDouble(final int column) throws SQLException {
    final var res = resultSet.getDouble(column);
    return resultSet.wasNull() ? null : res;
  }

  public double getDoublePrimitive(final int column) throws SQLException {
    final var res = resultSet.getDouble(column);
    if (resultSet.wasNull()) throw new SQLException("Column " + column + " is null");
    return res;
  }

  public Float getFloat(final int column) throws SQLException {
    final var res = resultSet.getFloat(column);
    return resultSet.wasNull() ? null : res;
  }

  public float getFloatPrimitive(final int column) throws SQLException {
    final var res = resultSet.getFloat(column);
    if (resultSet.wasNull()) throw new SQLException("Column " + column + " is null");
    return res;
  }

  public URI getUri(final int column) throws SQLException {
    final var res = resultSet.getString(column);
    return resultSet.wasNull() ? null : URI.create(res);
  }

  public Instant getInstant(final int column) throws SQLException {
    final var res = resultSet.getTimestamp(column);
    return resultSet.wasNull() ? null : res.toInstant();
  }

}
