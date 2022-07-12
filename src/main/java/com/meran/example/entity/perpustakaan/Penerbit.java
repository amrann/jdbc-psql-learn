package com.meran.example.entity.perpustakaan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"bukuList"})
public class Penerbit {
  private String id;
  private String nama;
  private String alamat;
  private List<Buku> bukuList = new ArrayList<>(); // mewakili one to many relasi (1 penerbit bisa memiliki >= 1 buku)
}
