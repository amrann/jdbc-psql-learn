package com.meran.example.service.perpustakaan;

import com.meran.example.dao.perpustakaan.BukuDao;
import com.meran.example.dao.perpustakaan.PenulisBukuDao;
import com.meran.example.dao.perpustakaan.PenulisDao;
import com.meran.example.entity.perpustakaan.Buku;
import com.meran.example.entity.perpustakaan.Penulis;
import com.meran.example.entity.perpustakaan.PenulisBuku;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PenulisBukuService {
  private final BukuDao bukuDao;
  private final PenulisDao penulisDao;
  private final PenulisBukuDao penulisBukuDao;

  public PenulisBukuService(BukuDao bukuDao, PenulisDao penulisDao, PenulisBukuDao penulisBukuDao) {
    this.bukuDao = bukuDao;
    this.penulisDao = penulisDao;
    this.penulisBukuDao = penulisBukuDao;
  }

  public Buku simpanByBuku(Buku buku, Penulis penulis) throws SQLException {
    Optional<Buku> bukuOptional = bukuDao.findById(buku.getId());
    if (!bukuOptional.isPresent())
      buku = this.bukuDao.simpan(buku);

    Optional<Penulis> penulisOptional = penulisDao.findById(penulis.getId());
    if (!penulisOptional.isPresent())
      penulis = this.penulisDao.simpan(penulis);

    this.penulisBukuDao.simpan(new PenulisBuku(null, buku, penulis));
    return buku;
  }

  public Penulis simpanByPenulis(Penulis penulis, Buku... listBuku) throws SQLException {
    Optional<Penulis> penulisOptional = penulisDao.findById(penulis.getId());
    if (!penulisOptional.isPresent())
      penulis = this.penulisDao.simpan(penulis);

    List<Buku> bukuList = Arrays.asList(listBuku.clone());
    List<PenulisBuku> listPenulisBuku = new ArrayList<>();
    for (Buku buku : bukuList) {
      Optional<Buku> optionalBuku = bukuDao.findById(buku.getId());
      if (!optionalBuku.isPresent()){
        buku = this.bukuDao.simpan(buku);
        listPenulisBuku.add(new PenulisBuku(null, buku, penulis));
      } else {
        listPenulisBuku.add(new PenulisBuku(null, buku, penulis));
      }
    }
    this.penulisBukuDao.listSimpan(listPenulisBuku);
    return penulis;
  }

}
