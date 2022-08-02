package com.meran.example.dao.bank;

import com.meran.example.dao.CrudRepository;
import com.meran.example.entity.bank.BadanUsaha;
import com.meran.example.entity.bank.Nasabah;
import com.meran.example.entity.bank.Perorangan;
import com.sun.org.apache.xpath.internal.operations.Bool;
import sun.management.counter.perf.PerfByteArrayCounter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NasabahMultiTableDao implements CrudRepository<Nasabah, String> {
  private Connection connection;

  public NasabahMultiTableDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public Nasabah simpan(Nasabah value) throws SQLException {
    if (value instanceof BadanUsaha) {
      String query = "insert into bank.nasabah_badanusaha(nama, npwp, siup, created_date, created_by)\n" +
        "values (?, ?, ?, ?, ?)";
      PreparedStatement ps = connection.prepareStatement(query);
      BadanUsaha bu = (BadanUsaha) value;
      ps.setString(1, bu.getNama());
      ps.setString(2, bu.getNpwp());
      ps.setString(3, bu.getSiup());
      ps.setTimestamp(4, value.getCreatedDate());
      ps.setString(5, value.getCreatedBy());
      ps.executeUpdate();
      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next())
        value.setCif(rs.getString("cif"));
      ps.close();
      rs.close();
      return value;
    } else {
      String query = "insert into bank.nasabah_perorangan(nama, ktp, foto, created_date, created_by)\n" +
        "values (?, ?, ?, ?, ?)";
      PreparedStatement ps = connection.prepareStatement(query);
      Perorangan perorangan = (Perorangan) value;
      ps.setString(1, perorangan.getNama());
      ps.setString(2, perorangan.getKtp());
      ps.setString(3, perorangan.getKtp());
      ps.setTimestamp(4, value.getCreatedDate());
      ps.setString(5, value.getCreatedBy());
      ps.executeUpdate();
      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next())
        value.setCif(rs.getString("cif"));
      ps.close();
      rs.close();
      return value;
    }
  }

  @Override
  public Nasabah update(Nasabah value) throws SQLException {
    return null;
  }

  @Override
  @Deprecated
  public Boolean hapusById(String value) throws SQLException {
    return null;
  }

  // hapus data via object
  public Boolean hapusById(Nasabah value) throws SQLException {
    int rowDeleted = 0;
    if (value instanceof Perorangan) {
      String query = "delete from bank.nasabah_perorangan where cif = ?";
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, value.getCif());
      rowDeleted = ps.executeUpdate();
      ps.close();
    } else {
      String query = "delete from bank.nasabah_badanusaha where cif = ?";
      PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, value.getCif());
      rowDeleted = ps.executeUpdate();
      ps.close();
    }
    return rowDeleted >= 1;
  }

  @Override
  public Optional<Nasabah> findById(String value) throws SQLException {
    String query = "select cif as nasabah_id,\n" +
      "       nama             as nasabah_nama,\n" +
      "       null             as badanusaha_npwp,\n" +
      "       null             as badanusaha_siup,\n" +
      "       ktp              as perorangan_ktp,\n" +
      "       foto             as perorangan_foto,\n" +
      "       1                as nasabah_type,\n" +
      "       created_date     as nasabah_created_date,\n" +
      "       created_by       as nasabah_created_by,\n" +
      "       last_updated_date as nasabah_last_updated_date,\n" +
      "       last_updated_by   as nasabah_last_updated_by\n" +
      "from bank.nasabah_perorangan\n" +
      "where cif = ?\n" +
      "union\n" +
      "select cif as nasabah_id,\n" +
      "       nama             as nasabah_nama,\n" +
      "       npwp             as badanusaha_npwp,\n" +
      "       siup             as badanusaha_siup,\n" +
      "       null             as perorangan_ktp,\n" +
      "       null             as perorangan_foto,\n" +
      "       2                as nasabah_type,\n" +
      "       created_date     as nasabah_created_date,\n" +
      "       created_by       as nasabah_created_by,\n" +
      "       last_updated_date as nasabah_last_updated_date,\n" +
      "       last_updated_by   as nasabah_last_updated_by\n" +
      "from bank.nasabah_badanusaha\n" +
      "where cif = ?\n" +
      "limit 1"; // diambil cuman salah satu data yang ada
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, value);
    ps.setString(2, value);
    ResultSet rs = ps.executeQuery();
    if (!rs.next()) {
      ps.close();
      rs.close();
      return Optional.empty();
    }
    int typeNasabah = rs.getInt("nasabah_type");
    if (typeNasabah == 1) {
      Perorangan perorangan = new Perorangan();
      perorangan.setCif(rs.getString("nasabah_id"));
      perorangan.setNama(rs.getString("nasabah_nama"));
      perorangan.setKtp(rs.getString("perorangan_ktp"));
      perorangan.setFoto(rs.getString("perorangan_foto"));
      perorangan.setCreatedDate(rs.getTimestamp("nasabah_created_date"));
      perorangan.setCreatedBy(rs.getString("nasabah_created_by"));
      perorangan.setLastUpdateBy(rs.getString("nasabah_last_updated_by"));
      perorangan.setLastUpdateDate(rs.getTimestamp("nasabah_last_updated_date"));
      ps.close();
      rs.close();
      return Optional.of(perorangan);
    } else {
      BadanUsaha badanUsaha = new BadanUsaha();
      badanUsaha.setCif(rs.getString("nasabah_id"));
      badanUsaha.setNama(rs.getString("nasabah_nama"));
      badanUsaha.setNpwp(rs.getString("badanusaha_npwp"));
      badanUsaha.setSiup(rs.getString("badanusaha_siup"));
      badanUsaha.setCreatedDate(rs.getTimestamp("nasabah_created_date"));
      badanUsaha.setCreatedBy(rs.getString("nasabah_created_by"));
      badanUsaha.setLastUpdateBy(rs.getString("nasabah_last_updated_by"));
      badanUsaha.setLastUpdateDate(rs.getTimestamp("nasabah_last_updated_date"));
      ps.close();
      rs.close();
      return Optional.of(badanUsaha);
    }
  }

  @Override
  public List<Nasabah> findAll() throws SQLException {
    List<Nasabah> nasabahList = new ArrayList<>();
    String query = "select cif as nasabah_id,\n" +
      "       nama             as nasabah_nama,\n" +
      "       null             as badanusaha_npwp,\n" +
      "       null             as badanusaha_siup,\n" +
      "       ktp              as perorangan_ktp,\n" +
      "       foto             as perorangan_foto,\n" +
      "       1                as nasabah_type,\n" +
      "       created_date     as nasabah_created_date,\n" +
      "       created_by       as nasabah_created_by,\n" +
      "       last_updated_date as nasabah_last_updated_date,\n" +
      "       last_updated_by   as nasabah_last_updated_by\n" +
      "from bank.nasabah_perorangan\n" +
      "union\n" +
      "select cif as nasabah_id,\n" +
      "       nama             as nasabah_nama,\n" +
      "       npwp             as badanusaha_npwp,\n" +
      "       siup             as badanusaha_siup,\n" +
      "       null             as perorangan_ktp,\n" +
      "       null             as perorangan_foto,\n" +
      "       2                as nasabah_type,\n" +
      "       created_date     as nasabah_created_date,\n" +
      "       created_by       as nasabah_created_by,\n" +
      "       last_updated_date as nasabah_last_updated_date,\n" +
      "       last_updated_by   as nasabah_last_updated_by\n" +
      "from bank.nasabah_badanusaha";
    PreparedStatement ps = connection.prepareStatement(query);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      int typeNasabah = rs.getInt("nasabah_type");
      if (typeNasabah == 1) {
        Perorangan perorangan = new Perorangan();
        perorangan.setCif(rs.getString("nasabah_id"));
        perorangan.setNama(rs.getString("nasabah_nama"));
        perorangan.setKtp(rs.getString("perorangan_ktp"));
        perorangan.setFoto(rs.getString("perorangan_foto"));
        perorangan.setCreatedDate(rs.getTimestamp("nasabah_created_date"));
        perorangan.setCreatedBy(rs.getString("nasabah_created_by"));
        perorangan.setLastUpdateBy(rs.getString("nasabah_last_updated_by"));
        perorangan.setLastUpdateDate(rs.getTimestamp("nasabah_last_updated_date"));
        nasabahList.add(perorangan);
      } else {
        BadanUsaha badanUsaha = new BadanUsaha();
        badanUsaha.setCif(rs.getString("nasabah_id"));
        badanUsaha.setNama(rs.getString("nasabah_nama"));
        badanUsaha.setNpwp(rs.getString("badanusaha_npwp"));
        badanUsaha.setSiup(rs.getString("badanusaha_siup"));
        badanUsaha.setCreatedDate(rs.getTimestamp("nasabah_created_date"));
        badanUsaha.setCreatedBy(rs.getString("nasabah_created_by"));
        badanUsaha.setLastUpdateBy(rs.getString("nasabah_last_updated_by"));
        badanUsaha.setLastUpdateDate(rs.getTimestamp("nasabah_last_updated_date"));
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
