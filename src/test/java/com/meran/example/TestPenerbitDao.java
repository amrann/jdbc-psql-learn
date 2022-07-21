package com.meran.example;

import com.meran.example.config.DatasourceConfig;
import com.meran.example.dao.perpustakaan.PenerbitDao;
import com.meran.example.entity.perpustakaan.Penerbit;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class TestPenerbitDao extends TestCase {

  private DatasourceConfig config;

  @Override
  protected void setUp() throws Exception {
    this.config = new DatasourceConfig();
  }

  @Test
  public void testFindAll() throws SQLException {
    DataSource dataSource = this.config.getDataSource();
    Connection connection = dataSource.getConnection();
    log.info("status connected");

    PenerbitDao penerbitDao = new PenerbitDao(connection);
    List<Penerbit> listPenerbit = penerbitDao.findAll();

    listPenerbit.forEach(data -> {
      log.info("penerbit id: {} => {}", data.getId(), data.getBukuList());
    });

    assertEquals("jumlah data di database ", 2, listPenerbit.size());

    connection.close();
  }

  @Test
  public void testFindId() throws SQLException {
    DataSource dataSource = this.config.getDataSource();
    Connection connection = dataSource.getConnection();
    log.info("status connected");

    PenerbitDao penerbitDao = new PenerbitDao(connection);
    Optional<Penerbit> id001Optional = penerbitDao.findById("001");
    assertTrue("id 001 data ditemukan", id001Optional.isPresent());
    Penerbit penerbit = id001Optional.get();
    log.info("penerbit 001 : {}", penerbit);
    log.info("list buku 001: {}", penerbit.getBukuList());

    assertEquals("id 001 namanya adalah ", "Informatika", penerbit.getNama());
    assertEquals("jumlah buku di penerbit 001 adalah  ", penerbit.getBukuList().size(), 2);

    Optional<Penerbit> randomId = penerbitDao.findById(UUID.randomUUID().toString());
    assertFalse("id random tidak ditemukan", randomId.isPresent());

    connection.close();

  }
}
