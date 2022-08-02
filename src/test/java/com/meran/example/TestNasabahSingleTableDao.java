package com.meran.example;

import com.meran.example.config.DatasourceConfig;
import com.meran.example.dao.bank.NasabahSingleTableDao;
import com.meran.example.entity.bank.BadanUsaha;
import com.meran.example.entity.bank.Nasabah;
import com.meran.example.entity.bank.Perorangan;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TestNasabahSingleTableDao extends TestCase {
  private DatasourceConfig config;

  @Override
  protected void setUp() throws Exception {
    this.config = new DatasourceConfig();
  }

  @Test
  public void testSave() throws SQLException {
    DataSource dataSource = this.config.getDataSource();
    Connection connection = dataSource.getConnection();
//    connection.setAutoCommit(false);
    log.info("status connected");

    NasabahSingleTableDao nasabahDao = new NasabahSingleTableDao(connection);

    Perorangan perorangan = new Perorangan();
    perorangan.setNama("Amran Maruusy");
    perorangan.setKtp("730934234544355");
    perorangan.setCreatedBy("admin");
    perorangan.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));

    perorangan = (Perorangan) nasabahDao.simpan(perorangan);
    log.info("data nasabah perorangan : {}", perorangan);

    BadanUsaha badanUsaha = new BadanUsaha();
    badanUsaha.setNama("PT. Kuat Kita Bersama");
    badanUsaha.setNpwp("12345678");
    badanUsaha.setSiup("89798987");
    badanUsaha.setCreatedBy("admin");
    badanUsaha.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));

    badanUsaha = (BadanUsaha) nasabahDao.simpan(badanUsaha);
    log.info("data nasabah badan usaha : {}", badanUsaha);

    connection.close();
  }

  @Test
  public void testFindId() throws SQLException {
    DataSource dataSource = this.config.getDataSource();
    Connection connection = dataSource.getConnection();
    log.info("status connected");

    NasabahSingleTableDao nasabahDao = new NasabahSingleTableDao(connection);

    Optional<Nasabah> optionalNasabah = nasabahDao.findById("001");
    assertTrue("Data nasabah 001 di temukan", optionalNasabah.isPresent());
    assertTrue("Data nasabah 001 adalah perorangan ", optionalNasabah.get() instanceof Perorangan);

    optionalNasabah = nasabahDao.findById("002");
    assertTrue("Data nasabah 002 di temukan", optionalNasabah.isPresent());
    assertTrue("Data nasabah 002 adalah badan usaha ", optionalNasabah.get() instanceof BadanUsaha);

    connection.close();
  }

  @Test
  public void testFindAll() throws SQLException {
    DataSource dataSource = this.config.getDataSource();
    Connection connection = dataSource.getConnection();
    log.info("status connected");

    NasabahSingleTableDao nasabahDao = new NasabahSingleTableDao(connection);

    List<Nasabah> nasabahList = nasabahDao.findAll();
    long jumlahNsbhPerorangan = nasabahList.stream().filter(dt -> dt instanceof Perorangan).count();
    assertEquals("jumlah nasabah perorangan", 1, jumlahNsbhPerorangan);

    long jumlahNsbhBadanUsaha = nasabahList.stream().filter(dt -> dt instanceof BadanUsaha).count();
    assertEquals("jumlah nasabah badan usaha", 1, jumlahNsbhBadanUsaha);

    connection.close();
  }
}
