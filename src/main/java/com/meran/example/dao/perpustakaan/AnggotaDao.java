package com.meran.example.dao.perpustakaan;

import com.meran.example.dao.CrudRepository;
import com.meran.example.entity.perpustakaan.Anggota;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AnggotaDao implements CrudRepository<Anggota, String> {
  private Connection connection;

  public AnggotaDao (Connection connection) {
    this.connection = connection;
  }

  @Override
  public Anggota simpan(Anggota value) throws SQLException {
    return null;
  }

  @Override
  public Anggota update(Anggota value) throws SQLException {
    return null;
  }

  @Override
  public Boolean hapusById(String value) throws SQLException {
    return null;
  }

  @Override
  public Optional<Anggota> findById(String value) throws SQLException {
    String query = "select * from perpustakaan.anggota where id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, value);
    ResultSet rs = ps.executeQuery();
    if (!rs.next()) {
      rs.close();
      ps.close();
      return Optional.empty();
    }
    Anggota data = new Anggota(
      rs.getString("id"),
      rs.getString("nomor_ktp"),
      rs.getString("nama"),
      rs.getString("alamat")
    );
    rs.close();
    ps.close();
    return Optional.of(data);
  }

  @Override
  public List<Anggota> findAll() throws SQLException {
    return null;
  }

  @Override
  public List<Anggota> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Anggota param) throws SQLException {
    return null;
  }
}
