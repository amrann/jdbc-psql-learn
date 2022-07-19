package com.meran.example.dao.perpustakaan;

import com.meran.example.dao.CrudRepository;
import com.meran.example.entity.perpustakaan.Buku;
import com.meran.example.entity.perpustakaan.Penerbit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BukuDao implements CrudRepository<Buku, String> {
  private Connection connection;

  public BukuDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public Buku simpan(Buku value) throws SQLException {
    String query = "insert into perpustakaan.buku(nama, isbn, penerbit_id) values (?, ?, ?)";
    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    ps.setString(1, value.getNama());
    ps.setString(2, value.getIsbn());
    // value Penerbit dipertimbangkan karena class Buku berelasi dengan Penerbit
    if (value.getPenerbit() != null) {
      ps.setNull(3, Types.VARCHAR);
    } else {
      ps.setString(3, value.getPenerbit().getId());
    }
    int row = ps.executeUpdate();
    ResultSet rs = ps.getGeneratedKeys();
    if (rs.next()) {
      value.setId(rs.getString("id"));
    }
    ps.close();
    return value;
  }

  @Override
  public Buku update(Buku value) throws SQLException {
    String query = "update perpustakaan.buku set nama = ?, isbn = ?, penerbit_id = ? where id = ?";
    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    ps.setString(1, value.getNama());
    ps.setString(2, value.getIsbn());
    if (value.getPenerbit() != null) {
      ps.setNull(3, Types.VARCHAR);
    } else {
      ps.setString(3, value.getPenerbit().getId());
    }
    ps.setString(4, value.getId());
    int row = ps.executeUpdate();
    ps.close();
    return value;
  }

  @Override
  public Boolean hapusById(String value) throws SQLException {
    String query = "delete from perpustakaan.buku where id = ?";
    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    ps.setString(1, value);
    int row = ps.executeUpdate();
    ps.close();
    return row >= 1;
  }

  @Override
  public Optional<Buku> findById(String value) throws SQLException {
    //language=POSTGRES-SQL
    String query = "select b.id as bukuId,\n" +
      "  b.nama as bukuNama,\n" +
      "  b.isbn as isbn,\n" +
      "  p.id as penerbitId,\n" +
      "  p.nama as penerbitNama,\n" +
      "  p.alamat as penerbitAlamat\n" +
      "from perpustakaan.buku b left join perpustakaan.penerbit p\n" +
      "on (b.penerbit_id = p.id)\n" +
      "where b.id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, value);
    ResultSet rs = ps.executeQuery();
    if (!rs.next()) {
      rs.close();
      ps.close();
      return Optional.empty();
    }
    Buku data = new Buku(
      rs.getString("bukuId"),
      rs.getString("bukuNama"),
      rs.getString("isbn"),
      new Penerbit(
        rs.getString("penerbitId"),
        rs.getString("penerbitNama"),
        rs.getString("penerbitAlamat"),
        new ArrayList<>()
      )
    );
    ps.close();
    rs.close();
    return Optional.of(data);
  }

  @Override
  public List<Buku> findAll() throws SQLException {
    List<Buku> list = new ArrayList<>();
    //language=POSTGRES-SQL
    String query = "select b.id as bukuId,\n" +
      "  b.nama as bukuNama,\n" +
      "  b.isbn as isbn,\n" +
      "  p.id as penerbitId,\n" +
      "  p.nama as penerbitNama,\n" +
      "  p.alamat as penerbitAlamat\n" +
      "from perpustakaan.buku b left join perpustakaan.penerbit p\n" +
      "on (b.penerbit_id = p.id)";

    PreparedStatement ps = (PreparedStatement) connection.createStatement();
    ResultSet rs = ps.executeQuery(query);

    while (rs.next()) {
      Buku data = new Buku(
        rs.getString("bukuId"),
        rs.getString("bukuNama"),
        rs.getString("isbn"),
        new Penerbit(
          rs.getString("penerbitId"),
          rs.getString("penerbitNama"),
          rs.getString("penerbitAlamat"),
          new ArrayList<>()
        )
      );
      list.add(data);
    }
    ps.close();
    rs.close();
    return list;
  }

  @Override
  public List<Buku> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Buku param) throws SQLException {
    return null;
  }
}
