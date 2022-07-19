package com.meran.example.entity.perpustakaan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaksiDetail {
  private String id;
  private Transaksi transaksi; // relasi ke table Transaksi
  private Buku buku; // relasi ke table Buku
  private Date tglKembali;
}
