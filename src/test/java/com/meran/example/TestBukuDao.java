package com.meran.example;

import com.meran.example.config.DatasourceConfig;
import com.meran.example.dao.perpustakaan.BukuDao;
import com.meran.example.entity.perpustakaan.Buku;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class TestBukuDao extends TestCase {
  private DatasourceConfig config;

  @Override
  protected void setUp() throws Exception {
    this.config = new DatasourceConfig();
  }

  @Test
  public void testHapus() throws SQLException {
    DataSource dataSource = this.config.getDataSource();
    Connection connection = dataSource.getConnection();
    log.info("status connected");

    BukuDao bukuDao = new BukuDao(connection);

    bukuDao.hapusById("002");

    connection.close();
  }

  @Test
  public void testFindById() throws SQLException {
    DataSource dataSource = this.config.getDataSource();
    Connection connection = dataSource.getConnection();
    log.info("status connected");

    BukuDao bukuDao = new BukuDao(connection);
    Optional<Buku> id001Optional = bukuDao.findById("001");

    assertTrue("id 001 data ditemukan", id001Optional.isPresent());
    Buku buku = id001Optional.get();
    assertEquals("id 001 namanya adalah ", "Pemograman Java", buku.getNama());
    assertEquals("id 001 penerbitnya adalah ", "Informatika", buku.getPenerbit().getNama());

    assertEquals("jumlah penulis pada buku ", buku.getListPenulis().size(), 2);
    log.info("list penulis : {}", buku.getListPenulis());

    Optional<Buku> randomId = bukuDao.findById(UUID.randomUUID().toString());
    assertFalse("id random tidak ditemukan", randomId.isPresent());
    connection.close();
  }
}
