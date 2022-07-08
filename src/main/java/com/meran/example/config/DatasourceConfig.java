package com.meran.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.IIOException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class DatasourceConfig {
  private final HikariConfig config;

  public DatasourceConfig() {
    Properties prop = new Properties();
    InputStream inputStream = DatasourceConfig.class.getResourceAsStream("/application.properties");
    try {
      prop.load(inputStream);
      inputStream.close();
    } catch (IOException e) {
      log.error("tidak bisa load file property", e);
    }
    this.config = new HikariConfig();
    config.setJdbcUrl(prop.getProperty("jdbc.url")); // ngambil dari application.properties
    config.setUsername(prop.getProperty("jdbc.username")); // ngambil dari application.properties
    config.setPassword(prop.getProperty("jdbc.password")); // ngambil dari application.properties
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
  }

  public DataSource getDataSource() {
    return new HikariDataSource(config);
  }
}
