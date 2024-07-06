package com.urbanhop.demo.service;

import com.urbanhop.demo.entity.CategoriaEntity;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<CategoriaEntity> lstCategorias();
    Optional<CategoriaEntity> findById(Long id);
    CategoriaEntity save(CategoriaEntity categoria);
    void deleteById(Long id);
}
