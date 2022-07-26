package com.meran.example.entity.perpustakaan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PenulisBuku {
  private String id;
  private Buku buku;
  private Penulis penulis;
}
