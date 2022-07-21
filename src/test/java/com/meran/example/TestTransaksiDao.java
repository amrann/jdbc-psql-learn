package com.meran.example;

import com.meran.example.config.DatasourceConfig;
import com.meran.example.dao.perpustakaan.AnggotaDao;
import com.meran.example.dao.perpustakaan.BukuDao;
import com.meran.example.dao.perpustakaan.TransaksiDao;
import com.meran.example.entity.perpustakaan.Anggota;
import com.meran.example.entity.perpustakaan.Buku;
import com.meran.example.entity.perpustakaan.Transaksi;
import com.meran.example.entity.perpustakaan.TransaksiDetail;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TestTransaksiDao extends TestCase {
  private DatasourceConfig config;

  @Override
  protected void setUp() throws Exception {
    this.config = new DatasourceConfig();
  }

  @Test
  public void testSave() throws SQLException {
    DataSource dataSource = this.config.getDataSource();
    Connection connection = dataSource.getConnection();
    connection.setAutoCommit(false);
    log.info("status connected");

    AnggotaDao anggotaDao = new AnggotaDao(connection);
    Optional<Anggota> meran = anggotaDao.findById("002");

    BukuDao bukuDao = new BukuDao(connection);
    Optional<Buku> java = bukuDao.findById("001");
    Optional<Buku> java2 = bukuDao.findById("003");

    LocalDate tgglPinjam = LocalDate.now();
    LocalDate tgglKembali = tgglPinjam.plusWeeks(1);

    TransaksiDao transaksiDao = new TransaksiDao(connection);

    Transaksi transaksi = new Transaksi(
      null,
      Date.valueOf(tgglPinjam),
      meran.orElse(null),
      Arrays.asList(
        new TransaksiDetail(
          null,
          null,
          java.orElse(null),
          Date.valueOf(tgglKembali)
        ),
        new TransaksiDetail(
          null,
          null,
          java2.orElse(null),
          Date.valueOf(tgglKembali)
        )
      )
    );


    transaksi = transaksiDao.simpan(transaksi);
    connection.commit();

    Optional<Transaksi> dataSimpan = transaksiDao.findById(transaksi.getId());
    if (dataSimpan.isPresent()) {
      Transaksi tr = dataSimpan.get();
      log.info("data transaksi {}", tr);
      log.info("data detail transaksi {}", tr.getTransaksiDetails());
    }
    connection.close();
  }
}
