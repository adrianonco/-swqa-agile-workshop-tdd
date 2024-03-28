package edu.upc.talent.swqa.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class HikariCP {

  private HikariCP() {}

  public static HikariDataSource getDataSource(final String jdbcUrl, final String username, final String password) {
    final var config = new HikariConfig();
    config.setJdbcUrl(jdbcUrl);
    config.setUsername(username);
    config.setPassword(password);
    config.setDriverClassName(org.postgresql.Driver.class.getName());
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    return new HikariDataSource(config);
  }

}
