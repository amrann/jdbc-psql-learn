package com.meran.example;

import com.meran.example.config.DatasourceConfig;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
public class DatasourceConfigTest extends TestCase {
  private DatasourceConfig config;

  @Override
  protected void setUp() throws Exception {
    this.config = new DatasourceConfig(
      "jdbc:postgresql://localhost:5432/postgres",
      "postgres",
      "admin123");
  }

  @Test
  public void testConnectionToDB() throws Exception{
    DataSource dataSource = this.config.getDataSource();
    Connection connect = dataSource.getConnection();
    log.info("status connected");
  }
}
