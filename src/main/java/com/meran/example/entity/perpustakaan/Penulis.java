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
@ToString(exclude = {"listBuku"})
public class Penulis {

  public Penulis(String id) {
    this.id = id;
  }

  private String id;
  private String nama;
  private String alamat;
  private List<Buku> listBuku = new ArrayList<>();
}
