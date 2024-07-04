package com.urbanhop.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urbanhop.demo.entity.ProductoEntity;
import com.urbanhop.demo.repository.ProductoRepository;
import com.urbanhop.demo.service.ProductoService;

import jakarta.transaction.Transactional;

@Service
public class ProductoServiceImpl  implements ProductoService{

	@Autowired
	private ProductoRepository productoRepository;
	
	@Override
	public void crearProducto(ProductoEntity productoEntity) {
		productoRepository.save(productoEntity);		
	}
	
	@Override
	public void eliminarProducto(Integer productoId) {
		productoRepository.deleteById(productoId);		
	}

	@Override
	public void actualizarProducto(ProductoEntity productoEntity) {
		productoRepository.save(productoEntity);
	}

	@Override
    @Transactional
	public List<ProductoEntity> lstProductos() {
		/**
		 * retorna una lista de productos obtenida desde el repositorio.
		 * Si la lista obtenida es nula, se devuelve una lista vacía.
		 */
	    List<ProductoEntity> lstProductos = new ArrayList<>();
	    return productoRepository.findAll() != null ? productoRepository.findAll() : lstProductos;
	}

	@Override
	public ProductoEntity findByProductoId(Integer productoId) {
		return productoRepository.findByProductoId(productoId);
	}

	
}
