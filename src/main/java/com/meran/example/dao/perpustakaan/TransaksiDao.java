package com.meran.example.dao.perpustakaan;

import com.meran.example.dao.CrudRepository;
import com.meran.example.entity.perpustakaan.*;

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

    String queryDetail = "insert into perpustakaan.transaksi_detail(transaksi_id, buku_id, tgl_kembali) values (?, ?, ?)";
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

  @Override
  public Boolean hapusById(String value) throws SQLException {
    return null;
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
      "  trxD.buku_id as transaksidetail_buku_id,\n" +
      "  trxD.tgl_kembali as transaksidetail_tgl_kembali,\n" +
      "  book.id as buku_id,\n" +
      "  book.nama as buku_nama,\n" +
      "  book.isbn as buku_isbn,\n" +
      "  book.penerbit_id as buku_penerbit_id,\n" +
      "  pub.nama as penerbit_nama,\n" +
      "  pub.alamat as penerbit_alamat\n" +
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
          )
        ),
        rs.getDate("transaksidetail_tgl_kembali")
      );
      listTransaksiDetail.add(trx_detail);
    }
    ps.close();
    rs.close();
    return listTransaksiDetail;
  }
}
