package com.urbanhop.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urbanhop.demo.entity.CategoriaEntity;
import com.urbanhop.demo.repository.CategoriaRepository;
import com.urbanhop.demo.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService{
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	@Override
	public List<CategoriaEntity> lstCategorias() {
		List<CategoriaEntity> lstCategorias = new ArrayList<CategoriaEntity>();
		return categoriaRepository.findAll() != null ? categoriaRepository.findAll() : lstCategorias;
	}

}