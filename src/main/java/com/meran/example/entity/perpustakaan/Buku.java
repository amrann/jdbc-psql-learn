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
@ToString(exclude = {"listPenulis"})
public class Buku {
  private String id;
  private String nama;
  private String isbn;
  private Penerbit penerbit; // mewakili penerbit_id yang digunakan utk berelasi ke Penerbit
  // meskipun tipe data di table String (varchar), disini diset object
  // mewakili one to one relation (1 buku memiliki 1 penerbit)

  private List<Penulis> listPenulis = new ArrayList<>(); //buku mempunyai banyak penulis
}
