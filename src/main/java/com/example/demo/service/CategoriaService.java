package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.CategoriaEntity;

public interface CategoriaService {
	List<CategoriaEntity> buscarTodasCategorias();
	CategoriaEntity buscarCategoriaPorId(Integer id);
	void save(CategoriaEntity categoria);
}
