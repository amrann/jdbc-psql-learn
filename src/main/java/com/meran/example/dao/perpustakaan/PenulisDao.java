package com.meran.example.dao.perpustakaan;

import com.meran.example.dao.CrudRepository;
import com.meran.example.entity.perpustakaan.Buku;
import com.meran.example.entity.perpustakaan.Penerbit;
import com.meran.example.entity.perpustakaan.Penulis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PenulisDao implements CrudRepository<Penulis, String> {
  private Connection connection;

  public PenulisDao (Connection connection) {
    this.connection = connection;
  }

  @Override
  public Penulis simpan(Penulis value) throws SQLException {
    return null;
  }

  @Override
  public Penulis update(Penulis value) throws SQLException {
    return null;
  }

  @Override
  public Boolean hapusById(String value) throws SQLException {
    return null;
  }

  @Override
  public Optional<Penulis> findById(String value) throws SQLException {
    String query = "select * from perpustakaan.penulis where id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, value);
    ResultSet rs = ps.executeQuery();
    if (!rs.next()) {
      ps.close();
      rs.close();
      Optional.empty();
    }
    Penulis penulis = new Penulis(
      rs.getString("id"),
      rs.getString("nama"),
      rs.getString("alamat"),
      this.findBukuByPenulisId(rs.getString("id"))
    );
    ps.close();
    rs.close();
    return Optional.of(penulis);
  }

  public List<Buku> findBukuByPenulisId(String value) throws SQLException {
    List<Buku> bukuList = new ArrayList<>();
    String query = "select pb.id as pelisbuk_id,\n" +
      "       book.id          as book_id,\n" +
      "       book.nama        as book_nama,\n" +
      "       book.isbn        as book_isbn,\n" +
      "       book.penerbit_id as book_penerbit_id,\n" +
      "       pub.nama         as penerbit_nama,\n" +
      "       pub.alamat       as penerbit_alamat\n" +
      "from perpustakaan.penulis_buku pb\n" +
      "left join perpustakaan.buku book on pb.buku_id = book.id\n" +
      "  left join perpustakaan.penerbit pub on book.penerbit_id = pub.id\n" +
      "where pb.penulis_id = ?";

    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, value);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Buku buku = new Buku(
        rs.getString("book_id"),
        rs.getString("book_nama"),
        rs.getString("book_isbn"),
        new Penerbit(
          rs.getString("book_penerbit_id"),
          rs.getString("penerbit_nama"),
          rs.getString("penerbit_alamat"),
          new ArrayList<>()
        ),
        new ArrayList<>()
      );
      bukuList.add(buku);
    }
    ps.close();
    rs.close();
    return bukuList;
  }

  @Override
  public List<Penulis> findAll() throws SQLException {
    return null;
  }

  @Override
  public List<Penulis> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Penulis param) throws SQLException {
    return null;
  }
}
