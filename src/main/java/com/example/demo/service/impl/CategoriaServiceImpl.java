package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CategoriaEntity;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService{

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Override
	public List<CategoriaEntity> buscarTodasCategorias() {
		return categoriaRepository.findAll();
	}

	@Override
	public CategoriaEntity buscarCategoriaPorId(Integer id) {
		return categoriaRepository.findById(id).orElse(null);
	}

	@Override
	public void save(CategoriaEntity categoria) {
		categoriaRepository.save(categoria);
		
	}

}
