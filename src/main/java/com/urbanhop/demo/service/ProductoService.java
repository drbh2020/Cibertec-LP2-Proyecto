package com.urbanhop.demo.service;

import java.util.List;

import com.urbanhop.demo.entity.ProductoEntity;

public interface ProductoService {
	void crearProducto(ProductoEntity productoEntity);
	void eliminarProducto(Integer productoId);
	void actualizarProducto(ProductoEntity productoEntity);
	
	List<ProductoEntity> lstProductos();
	ProductoEntity findByProductoId(Integer productoId);	
}
