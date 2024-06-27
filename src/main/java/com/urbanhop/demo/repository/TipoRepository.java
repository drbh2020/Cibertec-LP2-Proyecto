package com.urbanhop.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urbanhop.demo.entity.TipoEntity;

@Repository
public interface TipoRepository extends JpaRepository<TipoEntity, Integer> {
    TipoEntity findByNombre(String nombre);
}
