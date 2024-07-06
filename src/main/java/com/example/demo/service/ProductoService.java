package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.ProductoEntity;

public interface ProductoService {
	List<ProductoEntity> buscarTodosProductos();
	ProductoEntity buscarProductoPorId(Long id);
	void save(ProductoEntity producto); 

}
