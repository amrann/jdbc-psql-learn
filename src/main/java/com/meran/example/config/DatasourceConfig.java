package com.meran.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class DatasourceConfig {
  private final HikariConfig config;

  public DatasourceConfig(String url, String username, String password) {
//    Properties prop = new Properties();
//    InputStream inp = DatasourceConfig.
    this.config = new HikariConfig();
    config.setJdbcUrl(url);
    config.setUsername(username);
    config.setPassword(password);
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
  }

  public DataSource getDataSource() {
    return new HikariDataSource(config);
  }
}
