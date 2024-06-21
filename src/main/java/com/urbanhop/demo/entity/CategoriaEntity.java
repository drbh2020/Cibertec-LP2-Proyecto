package com.urbanhop.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_categoria")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoriaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_categoria")
	private Long categoriaId;
	@Column(name = "nombre_categoria", length = 45, nullable = false)
	private String nombre;
	
	@ManyToMany
	@JoinTable(
		name = "tb_producto_categoria", 
		joinColumns = @JoinColumn(name = "categoria_id_categoria"),
		inverseJoinColumns = @JoinColumn(name = "producto_id_producto" )
	)
	private List<ProductoEntity> listaProductosCategorias = new ArrayList<>();
}
