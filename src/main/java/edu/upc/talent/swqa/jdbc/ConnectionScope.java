package edu.upc.talent.swqa.jdbc;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ConnectionScope {
  private final Connection connection;

  public ConnectionScope(final Connection connection) {
    this.connection = connection;
  }

  public <A> List<A> select(final String sql, final RowReader<A> reader, final Param... params) {
    try (final var stmt = connection.prepareStatement(sql)) {
      for (int i = 0; i < params.length; i++) params[i].set(stmt, i + 1);
      try (final var rs = stmt.executeQuery()) {
        final var res = new ArrayList<A>();
        while (rs.next()) {
          res.add(reader.apply(new ResultSetView(rs)));
        }
        return Collections.unmodifiableList(res);
      }
    } catch (final SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public int update(final String sql, final Param... params) {
    try (final var stmt = connection.prepareStatement(sql)) {
      for (int i = 0; i < params.length; i++) params[i].set(stmt, i + 1);
      return stmt.executeUpdate();
    } catch (final SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public <K> K insertAndGetKey(final String sql, final RowReader<K> keyReader, final Param... params) {
    try (final var stmt = connection.prepareStatement(sql, RETURN_GENERATED_KEYS)) {
      for (int i = 0; i < params.length; i++) params[i].set(stmt, i + 1);
      stmt.executeUpdate();
      try (final var keys = stmt.getGeneratedKeys()) {
        keys.next();
        return keyReader.apply(new ResultSetView(keys));
      }
    } catch (final SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public <A> A selectOne(final String sql, final RowReader<A> reader, final Param... params) {
    try (final var stmt = connection.prepareStatement(sql)) {
      for (int i = 0; i < params.length; i++) params[i].set(stmt, i + 1);
      try (final var rs = stmt.executeQuery()) {
        if (!rs.next()) throw new SQLException("No rows returned");
        final var result = reader.apply(new ResultSetView(rs));
        if (rs.next()) throw new SQLException("More than one row returned");
        return result;
      }
    } catch (final SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
}
