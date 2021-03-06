package com.meran.example.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
  public T simpan(T value) throws SQLException;

  public T update(T value) throws SQLException;

  public Boolean hapusById(ID value) throws SQLException;

  public Optional<T> findById(ID value) throws SQLException;

  public List<T> findAll() throws SQLException;

  public List<T> findAll(Long start, Long limit, Long orderIndex, String orderDirection, T param) throws SQLException;

}
