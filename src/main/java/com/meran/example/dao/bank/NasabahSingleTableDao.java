package com.meran.example.dao.bank;

import com.meran.example.dao.CrudRepository;
import com.meran.example.entity.bank.BadanUsaha;
import com.meran.example.entity.bank.Nasabah;
import com.meran.example.entity.bank.Perorangan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NasabahSingleTableDao implements CrudRepository<Nasabah, String> {
  private Connection connection;

  public NasabahSingleTableDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public Nasabah simpan(Nasabah value) throws SQLException {
    String query = "insert into bank.nasabah(nama, npwp, siup, ktp, foto, type, created_date, created_by) \n" +
      "values (?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    ps.setString(1, value.getNama());
    ps.setTimestamp(7, value.getCreatedDate());
    ps.setString(8, value.getCreatedBy());
    if (value instanceof BadanUsaha) {
      BadanUsaha badanUsaha = (BadanUsaha) value;
      ps.setString(2, badanUsaha.getNpwp());
      ps.setString(3, badanUsaha.getSiup());
      ps.setNull(4, Types.VARCHAR);
      ps.setNull(5, Types.VARCHAR);
      ps.setInt(6, 2);
    } else {
      Perorangan perorangan = (Perorangan) value;
      ps.setNull(2, Types.VARCHAR);
      ps.setNull(3, Types.VARCHAR);
      ps.setString(4, perorangan.getKtp());
      ps.setString(5, perorangan.getFoto());
      ps.setInt(6, 1);
    }
    ps.executeUpdate();
    ResultSet rs = ps.getGeneratedKeys();
    if (rs.next())
      value.setCif(rs.getString("cif"));
    ps.close();
    rs.close();
    return value;
  }

  @Override
  public Nasabah update(Nasabah value) throws SQLException {
    return null;
  }

  @Override
  public Boolean hapusById(String value) throws SQLException {
    return null;
  }

  @Override
  public Optional<Nasabah> findById(String value) throws SQLException {
    String query = "select * from bank.nasabah where cif = ?";
    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    ps.setString(1, value);
    ResultSet rs = ps.executeQuery();
    if (!rs.next()) {
      ps.close();
      rs.close();
      return Optional.empty();
    }
    int typeNasabah = rs.getInt("type");
    if (typeNasabah == 1) {
      Perorangan perorangan = new Perorangan();
      perorangan.setCif(rs.getString("cif"));
      perorangan.setNama(rs.getString("nama"));
      perorangan.setKtp(rs.getString("ktp"));
      perorangan.setFoto(rs.getString("foto"));
      perorangan.setCreatedDate(rs.getTimestamp("created_date"));
      perorangan.setCreatedBy(rs.getString("created_by"));
      perorangan.setLastUpdateBy(rs.getString("last_updated_by"));
      perorangan.setLastUpdateDate(rs.getTimestamp("last_updated_date"));
      ps.close();
      rs.close();
      return Optional.of(perorangan);
    } else {
      BadanUsaha badanUsaha = new BadanUsaha();
      badanUsaha.setCif(rs.getString("cif"));
      badanUsaha.setNama(rs.getString("nama"));
      badanUsaha.setNpwp(rs.getString("npwp"));
      badanUsaha.setSiup(rs.getString("siup"));
      badanUsaha.setCreatedDate(rs.getTimestamp("created_date"));
      badanUsaha.setCreatedBy(rs.getString("created_by"));
      badanUsaha.setLastUpdateBy(rs.getString("last_updated_by"));
      badanUsaha.setLastUpdateDate(rs.getTimestamp("last_updated_date"));
      ps.close();
      rs.close();
      return Optional.of(badanUsaha);
    }
  }

  @Override
  public List<Nasabah> findAll() throws SQLException {
    List<Nasabah> nasabahList = new ArrayList<>();
    String query = "select * from bank.nasabah";
    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      int typeNasabah = rs.getInt("type");
      if (typeNasabah == 1) {
        Perorangan perorangan = new Perorangan();
        perorangan.setCif(rs.getString("cif"));
        perorangan.setNama(rs.getString("nama"));
        perorangan.setKtp(rs.getString("ktp"));
        perorangan.setFoto(rs.getString("foto"));
        perorangan.setCreatedDate(rs.getTimestamp("created_date"));
        perorangan.setCreatedBy(rs.getString("created_by"));
        perorangan.setLastUpdateBy(rs.getString("last_updated_by"));
        perorangan.setLastUpdateDate(rs.getTimestamp("last_updated_date"));
        nasabahList.add(perorangan);
      } else {
        BadanUsaha badanUsaha = new BadanUsaha();
        badanUsaha.setCif(rs.getString("cif"));
        badanUsaha.setNama(rs.getString("nama"));
        badanUsaha.setNpwp(rs.getString("npwp"));
        badanUsaha.setSiup(rs.getString("siup"));
        badanUsaha.setCreatedDate(rs.getTimestamp("created_date"));
        badanUsaha.setCreatedBy(rs.getString("created_by"));
        badanUsaha.setLastUpdateBy(rs.getString("last_updated_by"));
        badanUsaha.setLastUpdateDate(rs.getTimestamp("last_updated_date"));
        nasabahList.add(badanUsaha);
      }
    }
    ps.close();
    rs.close();
    return nasabahList;
  }

  @Override
  public List<Nasabah> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Nasabah param) throws SQLException {
    return null;
  }
}
