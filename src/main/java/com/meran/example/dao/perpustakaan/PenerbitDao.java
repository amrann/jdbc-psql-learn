package com.meran.example.dao.perpustakaan;

import com.meran.example.dao.CrudRepository;
import com.meran.example.entity.perpustakaan.Buku;
import com.meran.example.entity.perpustakaan.Penerbit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PenerbitDao implements CrudRepository<Penerbit, String> {

  private Connection connection;

  public PenerbitDao (Connection connection) {
    this.connection = connection;
  }

  @Override
  public Penerbit simpan(Penerbit value) throws SQLException {
    String query = "insert into perpustakaan.penerbit(nama, alamat) values (?, ?)";
    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    ps.setString(1, value.getNama());
    ps.setString(2, value.getAlamat());
    ps.executeUpdate();
    ResultSet rs = ps.getGeneratedKeys();
    if (rs.next()) {
      value.setId(rs.getString("id"));
    }
    ps.close();
    return value;
  }

  public Penerbit update (Penerbit value) throws SQLException {
    String query = "update perpustakaan.penerbit set nama = ?, alamat = ? where id = ?";
    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    ps.setString(1, value.getNama());
    ps.setString(2, value.getAlamat());
    ps.setString(3, value.getId());
    ps.executeUpdate();
    ps.close();
    return value;
  }

  @Override
  public Boolean hapusById(String value) throws SQLException {
    String query = "delete from perpustakaan.penerbit where id = ? ";
    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    ps.setString(1, value);
    int row = ps.executeUpdate();
    ps.close();
    return row >= 1;
  }

  @Override
  public Optional<Penerbit> findById(String value) throws SQLException {
    //language=POSTGRES-SQL
    // menampilkan semua data yang telah berelasi
    String query = "select p.id as id,\n" +
      "  p.nama as nama,\n" +
      "  p.alamat as alamat,\n" +
      "  b.id as buku_id,\n" +
      "  b.nama as buku_nama,\n" +
      "  b.isbn as buku_isbn\n" +
      "from perpustakaan.penerbit p join perpustakaan.buku b on p.id = b.penerbit_id\n" +
      "where p.id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, value);
    ResultSet rs = ps.executeQuery();
    Penerbit penerbit = new Penerbit();
    List<Buku> listBuku = new ArrayList<>();
    while (rs.next()) {
      penerbit.setId(rs.getString("id"));
      penerbit.setNama(rs.getString("nama"));
      penerbit.setAlamat(rs.getString("alamat"));

      // object buku dibuat didalam while karena buku ini nnti yang datanya banyak (datanya beda-beda)
      // sedangkan object penerbit datanya mostly 1 jenis doang
      // ingat, relasi antara table Penerbit<>Buku itu one to many relation
      // 1 penerbit mempunyai banyak buku
      Buku buku = new Buku();
      buku.setId(rs.getString("buku_id"));
      buku.setNama(rs.getString("buku_nama"));
      buku.setIsbn(rs.getString("buku_isbn"));
      listBuku.add(buku);
    }
    penerbit.setBukuList(listBuku);
    ps.close();
    rs.close();
    return penerbit.getId() != null ? Optional.of(penerbit) : Optional.empty();
  }

//  @Override
//  public List<Penerbit> findAll() throws SQLException {
//    List<Penerbit> list = new ArrayList<>();
//    //language=POSTGRES-SQL
//    String query = "select id as id,\n" +
//      "  nama as nama,\n" +
//      "  alamat as alamat\n" +
//      "from perpustakaan.penerbit";
//    PreparedStatement ps = connection.prepareStatement(query);
//    ResultSet rs = ps.executeQuery();
//    while (rs.next()) {
//      Penerbit data = new Penerbit(
//        rs.getString("id"),
//        rs.getString("nama"),
//        rs.getString("alamat"),
//        new ArrayList<>()
//      );
//      list.add(data);
//    }
//    ps.close();
//    rs.close();
//    return null;
//  }

  @Override
  public List<Penerbit> findAll() throws SQLException {
    List<Penerbit> list = new ArrayList<>();
    String query = "select id as id,\n" +
      "  nama as nama,\n" +
      "  alamat as alamat\n" +
      "from perpustakaan.penerbit";
    PreparedStatement ps = connection.prepareStatement(query);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Penerbit data = new Penerbit(
        rs.getString("id"),
        rs.getString("nama"),
        rs.getString("alamat"),
        this.findByPenerbitId(rs.getString("id"))
      );
      list.add(data);
    }
    ps.close();
    rs.close();
    return list;
  }


  @Override
  public List<Penerbit> findAll(Long start, Long limit, Long orderIndex, String orderDirection, Penerbit param) throws SQLException {
    return null;
  }

  // optional method
  public List<Buku> findByPenerbitId(String value) throws SQLException {
    String query = "select b.id as buku_id,\n" +
      "  b.nama as buku_nama,\n" +
      "  b.isbn as buku_isbn\n" +
      "from perpustakaan.penerbit p join perpustakaan.buku b on p.id = b.penerbit_id\n" +
      "where p.id = ?";
    PreparedStatement ps = connection.prepareStatement(query);
    ps.setString(1, value);
    ResultSet rs = ps.executeQuery();
    List<Buku> listBuku = new ArrayList<>();
    while (rs.next()) {
      Buku buku = new Buku();
      buku.setId(rs.getString("buku_id"));
      buku.setNama(rs.getString("buku_nama"));
      buku.setIsbn(rs.getString("buku_isbn"));
      listBuku.add(buku);
    }
    ps.close();
    rs.close();
    return listBuku;
  }

}