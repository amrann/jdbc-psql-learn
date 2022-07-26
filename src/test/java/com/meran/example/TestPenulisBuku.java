package com.meran.example;

import com.meran.example.config.DatasourceConfig;
import com.meran.example.dao.perpustakaan.BukuDao;
import com.meran.example.dao.perpustakaan.PenulisBukuDao;
import com.meran.example.dao.perpustakaan.PenulisDao;
import com.meran.example.entity.perpustakaan.Buku;
import com.meran.example.entity.perpustakaan.Penulis;
import com.meran.example.service.perpustakaan.PenulisBukuService;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class TestPenulisBuku extends TestCase {
  private DatasourceConfig config;

  @Override
  protected void setUp() throws Exception {
    this.config = new DatasourceConfig();
  }

  @Test
  public void testSaveBukuDgnPenulis() throws SQLException {
    DataSource dataSource = config.getDataSource();
    Connection connection = dataSource.getConnection();
    connection.setAutoCommit(false);
    log.info("status connected");

    BukuDao bukuDao = new BukuDao(connection);
    PenulisDao penulisDao = new PenulisDao(connection);
    PenulisBukuDao penulisBukuDao = new PenulisBukuDao(connection);

    PenulisBukuService penulisBukuService = new PenulisBukuService(bukuDao, penulisDao, penulisBukuDao);
    penulisBukuService.simpanByBuku(new Buku("001"), new Penulis("003"));

    connection.commit();
    connection.close();
  }

  @Test
  public void testSavePenulisDgnBuku() throws SQLException {
    DataSource dataSource = config.getDataSource();
    Connection connection = dataSource.getConnection();
    connection.setAutoCommit(false);
    log.info("status connected");

    BukuDao bukuDao = new BukuDao(connection);
    PenulisDao penulisDao = new PenulisDao(connection);
    PenulisBukuDao penulisBukuDao = new PenulisBukuDao(connection);

    PenulisBukuService penulisBukuService = new PenulisBukuService(bukuDao, penulisDao, penulisBukuDao);
    penulisBukuService.simpanByPenulis(new Penulis("003"), new Buku("003"), new Buku("002"), new Buku("001"));
    penulisBukuService.simpanByPenulis(new Penulis("001"), new Buku("002"), new Buku("003"), new Buku("001"));

    connection.commit();
    connection.close();
  }

  @Test
  public void testHapus() throws SQLException {
    DataSource dataSource = config.getDataSource();
    Connection connection = dataSource.getConnection();
    log.info("status connected");

    PenulisBukuDao penulisBukuDao = new PenulisBukuDao(connection);
    penulisBukuDao.hapusById("850c2ecc-fc27-408c-a137-ffd09c3ff127");
    connection.close();

  }
}
