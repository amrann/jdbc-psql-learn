package com.meran.example.entity.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Nasabah {
  private String cif;
  private String nama;
  private Timestamp createdDate;
  private String createdBy;
  private Timestamp lastUpdateDate;
  private String lastUpdateBy;
}
