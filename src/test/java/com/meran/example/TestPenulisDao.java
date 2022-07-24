package com.meran.example;

import com.meran.example.config.DatasourceConfig;
import com.meran.example.dao.perpustakaan.BukuDao;
import com.meran.example.dao.perpustakaan.PenulisDao;
import com.meran.example.entity.perpustakaan.Buku;
import com.meran.example.entity.perpustakaan.Penulis;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class TestPenulisDao extends TestCase {
  private DatasourceConfig config;

  @Override
  protected void setUp() throws Exception {
    this.config = new DatasourceConfig();
  }

  @Test
  public void testFindById() throws SQLException {
    DataSource dataSource = this.config.getDataSource();
    Connection connection = dataSource.getConnection();
    log.info("status connected");

    PenulisDao penulisDao = new PenulisDao(connection);
    Optional<Penulis> id001Optional = penulisDao.findById("001");

    assertTrue("id 001 data ditemukan", id001Optional.isPresent());
    Penulis penulis = id001Optional.get();
    assertEquals("id 001 namanya adalah ", "Hera Kusnadi", penulis.getNama());

    assertEquals("jumlah penulis pada buku ", penulis.getListBuku().size(), 1);
    log.info("list buku : {}", penulis.getListBuku());

    connection.close();
  }
}
