package com.meran.example.dao.perpustakaan;

import com.meran.example.dao.CrudRepository;
import com.meran.example.entity.perpustakaan.PenulisBuku;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class PenulisBukuDao implements CrudRepository<PenulisBuku, String> {
  private Connection connection;

  public PenulisBukuDao (Connection connection) {
    this.connection = connection;
  }

  @Override
  public PenulisBuku simpan(PenulisBuku value) throws SQLException {
    String query = "insert into perpustakaan.penulis_buku(buku_id, penulis_id) values(?, ?)";
    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    if (value.getBuku() != null)
      ps.setString(1, value.getBuku().getId());
    else
      ps.setNull(1, Types.VARCHAR);

    if (value.getPenulis() != null)
      ps.setString(2, value.getPenulis().getId());
    else
      ps.setNull(2, Types.VARCHAR);

    ps.executeUpdate();
    ResultSet rs = ps.getGeneratedKeys();
    if (rs.next())
      value.setId(rs.getString("id"));
    ps.close();
    rs.close();

    return value;
  }

  public void listSimpan(List<PenulisBuku> list) throws SQLException {
    String query = "insert into perpustakaan.penulis_buku(buku_id, penulis_id) values(?, ?)";
    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    for (PenulisBuku value : list) {
      if (value.getBuku() != null)
        ps.setString(1, value.getBuku().getId());
      else
        ps.setNull(1, Types.VARCHAR);

      if (value.getPenulis() != null)
        ps.setString(2, value.getPenulis().getId());
      else
        ps.setNull(2, Types.VARCHAR);
      ps.addBatch();
    }
    ps.executeBatch();
    ps.close();
  }

  @Override
  @Deprecated
  public PenulisBuku update(PenulisBuku value) throws SQLException {
    return null;
  }

  @Override
  public Boolean hapusById(String value) throws SQLException {
    String query = "delete from perpustakaan.penulis_buku where id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, value);
    int row = ps.executeUpdate();
    ps.close();
    return row >= 1;
  }

  @Override
  @Deprecated
  public Optional<PenulisBuku> findById(String value) throws SQLException {
    return Optional.empty();
  }

  @Override
  @Deprecated
  public List<PenulisBuku> findAll() throws SQLException {
    return null;
  }

  @Override
  @Deprecated
  public List<PenulisBuku> findAll(Long start, Long limit, Long orderIndex, String orderDirection, PenulisBuku param) throws SQLException {
    return null;
  }
}
