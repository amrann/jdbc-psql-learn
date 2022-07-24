package com.meran.example.dao.perpustakaan;

import com.meran.example.dao.CrudRepository;
import com.meran.example.entity.perpustakaan.*;

import javax.swing.text.html.Option;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransaksiDao implements CrudRepository<Transaksi, String> {
  private Connection connection;

  public TransaksiDao (Connection connection) {
    this.connection = connection;
  }

  @Override
  public Transaksi simpan(Transaksi value) throws SQLException {
    String query = "insert into perpustakaan.transaksi(tgl_transaksi, anggota_id) values (?, ?)"; // query ini tidak perlu insert "id" karena "id" sudah diset create otomatis
    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    ps.setDate(1, value.getTglTransaksi());
    if (value.getAnggota() != null)
      ps.setString(2, value.getAnggota().getId());
    else
      ps.setNull(2, Types.VARCHAR);
    ps.executeUpdate(); // setelah execute, maka 'id' akan tergenerated
    ResultSet rs = ps.getGeneratedKeys();
    if (rs.next()) {
      value.setId(rs.getString("id")); // id kemudian diset pada value
    }

    String queryDetail = "insert into perpustakaan.transaksi_detail(transaksi_id, buku_id, tgl_kembali, is_return) \n" +
      "values (?, ?, ?, false)";
    ps = connection.prepareStatement(queryDetail);
    for (TransaksiDetail detail : value.getTransaksiDetails()) {
      ps.setString(1, value.getId());
      if(detail.getBuku() != null)
        ps.setString(2, detail.getBuku().getId());
      else
        ps.setNull(2, Types.VARCHAR);
      ps.setDate(3, detail.getTglKembali());
      ps.addBatch();
    }
    ps.executeBatch();
    ps.close();
    rs.close();
    return value;
  }

  @Override
  public Transaksi update(Transaksi value) throws SQLException {
    return null;
  }

  public TransaksiDetail update(TransaksiDetail value) throws SQLException {
    String query = "update perpustakaan.transaksi_detail\n" +
      "set is_return = true,\n" +
      "last_update_date = now() \n" +
      "where id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, value.getId());
    ps.executeUpdate();
    ps.close();
    return value;
  }

  public Optional<TransaksiDetail> findByTransactionIdAndBookId(String transactionId, String bookId) throws SQLException {
    //language=POSTGRES-SQL
    String query = "select trxD.id   as trxD_id,\n" +
      "       trxD.buku_id           as trxD_bukuid,\n" +
      "       trxD.tgl_kembali       as trxD_tglkembali,\n" +
      "       trxD.is_return         as trxD_isreturn,\n" +
      "       trxD.last_update_date  as trxD_lastupdate_date,\n" +
      "       book.id                as book_id,\n" +
      "       book.nama              as book_nama,\n" +
      "       book.isbn              as book_isbn,\n" +
      "       book.penerbit_id       as book_penerbitid,\n" +
      "       pub.nama               as pub_nama,\n" +
      "       pub.alamat             as pub_alamat,\n" +
      "       trx.id                 as trx_id,\n" +
      "       trx.tgl_transaksi      as trx_tgl_transaksi,\n" +
      "       trx.anggota_id         as trx_anggota_id,\n" +
      "       agt.nomor_ktp          as agt_no_ktp,\n" +
      "       agt.nama               as agt_nama,\n" +
      "       agt.alamat             as agt_alamat\n" +
      "from perpustakaan.transaksi_detail trxD\n" +
      "    left join perpustakaan.transaksi trx on trxD.transaksi_id = trx.id\n" +
      "    left join perpustakaan.buku book on trxD.buku_id = book.id\n" +
      "    left join perpustakaan.penerbit pub on book.penerbit_id = pub.id\n" +
      "    left join perpustakaan.anggota agt on trx.anggota_id = agt.id\n" +
      "where trxD.transaksi_id = ? \n" +
      "    and trxD.buku_id = ?\n" +
      "    and trxD.is_return = false";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, transactionId);
    ps.setString(2, bookId);
    ResultSet rs = ps.executeQuery();
    if (!rs.next()) {
      rs.close();
      ps.close();
      return Optional.empty();
    }
    TransaksiDetail transaksiDetail = new TransaksiDetail(
      rs.getString("trxD_id"),
      new Transaksi(
        rs.getString("trx_id"),
        rs.getDate("trx_tgl_transaksi"),
        new Anggota(
          rs.getString("trx_anggota_id"),
          rs.getString("agt_no_ktp"),
          rs.getString("agt_nama"),
          rs.getString("agt_alamat")
        ),
        null
      ),
      new Buku(
        rs.getString("book_id"),
        rs.getString("book_nama"),
        rs.getString("book_isbn"),
        new Penerbit(
          rs.getString("book_penerbitid"),
          rs.getString("pub_nama"),
          rs.getString("pub_alamat"),
          new ArrayList<>()
        ),
        new ArrayList<>()
      ),
      rs.getDate("trxD_tglkembali"),
      rs.getBoolean("trxD_isreturn"),
      rs.getDate("trxD_lastupdate_date")
    );
    ps.close();
    rs.close();
    return Optional.of(transaksiDetail);
  }

  @Override
  public Boolean hapusById(String value) throws SQLException {
    // table Transaksi mempunyai relasi on delete cascade, sehingga
    // ketika data transaksi dihapus, maka data yang berelasi di table TransaksiDetail akan ikut terhapus juga
    String query = "delete from perpustakaan.transaksi where id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, value);
    int row = ps.executeUpdate();
    return row >= 1;
  }

  @Override
  public Optional<Transaksi> findById(String value) throws SQLException {
    String query = "select trx.id as id,\n" +
      "  trx.tgl_transaksi as tanggal_transaksi,\n" +
      "  agt.id as anggota_id,\n" +
      "  agt.nomor_ktp as anggota_no_ktp,\n" +
      "  agt.nama as anggota_nama,\n" +
      "  agt.alamat as anggota_alamat\n" +
      "from perpustakaan.transaksi trx left join perpustakaan.anggota agt on trx.anggota_id = agt.id\n" +
      "where trx.id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, value);
    ResultSet rs = ps.executeQuery();
    if (!rs.next()) {
      ps.close();
      rs.close();
      return Optional.empty();
    }
    Transaksi transaksi = new Transaksi(
      rs.getString("id"),
      rs.getDate("tanggal_transaksi"),
      new Anggota(
        rs.getString("anggota_id"),
        rs.getString("anggota_no_ktp"),
        rs.getString("anggota_nama"),
        rs.getString("anggota_alamat")
      ),
      this.findByTransactionId(rs.getString("id"))
    );
    return Optional.of(transaksi);
  }

  @Override
  public List<Transaksi> findAll() throws SQLException {
    return null;
  }


  @Override
  public List<Transaksi> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Transaksi param) throws SQLException {
    return null;
  }

  // optional method
  public List<TransaksiDetail> findByTransactionId(String value) throws SQLException {
    List<TransaksiDetail> listTransaksiDetail = new ArrayList<>();
    String query = "select trxD.id as id,\n" +
      "  trxD.buku_id              as transaksidetail_buku_id,\n" +
      "  trxD.tgl_kembali          as transaksidetail_tgl_kembali,\n" +
      "  trxD.is_return            as transaksidetail_status_kembali,\n" +
      "  trxD.last_update_date     as transaksidetail_lastupdate_date,\n" +
      "  book.id                   as buku_id,\n" +
      "  book.nama                 as buku_nama,\n" +
      "  book.isbn                 as buku_isbn,\n" +
      "  book.penerbit_id          as buku_penerbit_id,\n" +
      "  pub.nama                  as penerbit_nama,\n" +
      "  pub.alamat                as penerbit_alamat\n" +
      "from perpustakaan.transaksi_detail trxD left join perpustakaan.buku book on trxD.buku_id = book.id\n" +
      "left join perpustakaan.penerbit pub on book.penerbit_id = pub.id\n" +
      "where trxD.transaksi_id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, value);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      TransaksiDetail trx_detail = new TransaksiDetail(
        rs.getString("id"),
        null,
        new Buku(
          rs.getString("buku_id"),
          rs.getString("buku_nama"),
          rs.getString("buku_isbn"),
          new Penerbit(
            rs.getString("buku_penerbit_id"),
            rs.getString("penerbit_nama"),
            rs.getString("penerbit_alamat"),
            new ArrayList<>()
          ),
          new ArrayList<>()
        ),
        rs.getDate("transaksidetail_tgl_kembali"),
        rs.getBoolean("transaksidetail_status_kembali"),
        rs.getDate("transaksidetail_lastupdate_date")
      );
      listTransaksiDetail.add(trx_detail);
    }
    ps.close();
    rs.close();
    return listTransaksiDetail;
  }
}
