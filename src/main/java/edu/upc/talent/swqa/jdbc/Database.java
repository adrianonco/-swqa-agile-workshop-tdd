package edu.upc.talent.swqa.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Database implements AutoCloseable {

  private final DataSource dataSource;

  public Database(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void withConnection(final Consumer<ConnectionScope> f) {
    withConnectionGet((conn) -> {
      f.accept(conn);
      return null;
    });
  }

  public <A> A withConnectionGet(final Function<ConnectionScope, A> f) {
    try (final Connection conn = dataSource.getConnection()) {
      return f.apply(new ConnectionScope(conn));
    } catch (final SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public <A> A inTransaction(final Function<ConnectionScope, A> f) {
    try (final Connection conn = dataSource.getConnection()) {
      conn.setAutoCommit(false);
      final var res = f.apply(new ConnectionScope(conn));
      conn.commit();
      return res;
    } catch (final SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int update(final String sql, final Param... params) {
    return withConnectionGet((conn) -> conn.update(sql, params));
  }


  public <A> Set<A> selectToSet(final String sql, final RowReader<A> reader, final Param... params) {
    return new HashSet<>(select(sql, reader, params));
  }

  public <A> List<A> select(final String sql, final RowReader<A> reader, final Param... params) {
    return withConnectionGet((conn) -> conn.select(sql, reader, params));
  }

  public <A> A selectOne(final String sql, final RowReader<A> reader, final Param... params) {
    return withConnectionGet((conn) -> conn.selectOne(sql, reader, params));
  }

  public <K> K insertAndGetKey(final String sql, final RowReader<K> keyReader, final Param... params) {
    return withConnectionGet((conn) -> conn.insertAndGetKey(sql, keyReader, params));
  }

  @Override
  public void close() throws Exception {
    if (dataSource instanceof AutoCloseable)
      ((AutoCloseable) dataSource).close();
  }
}