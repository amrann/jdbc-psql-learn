package com.meran.example.entity.perpustakaan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"transaksiDetails"})
public class Transaksi {
  private String id;
  private Date tglTransaksi;
  private Anggota anggota;
  private List<TransaksiDetail> transaksiDetails = new ArrayList<>(); // mewakili one to many relation
}
