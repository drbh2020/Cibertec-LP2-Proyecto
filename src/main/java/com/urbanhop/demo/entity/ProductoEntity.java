package com.urbanhop.demo.entity;

import java.util.ArrayList;
import java.util.List;

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
@Table(name = "tb_producto")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_producto")
	private Long productoId;
	
	@Column(name = "nombre", length = 45, nullable = false)
	private String nombre;
	
	@Column(name = "stock", nullable = false)
	private Integer stock;
	
	@Column(name = "precio", nullable = false)
	private Double precio;
	
	@Column(name = "talla", length = 45, nullable = false)
	private String talla;
	
	@Column(name = "estado", nullable = false)
	private Boolean estado;
	
	@ManyToMany(mappedBy = "listaProductosCategorias")
	private List<CategoriaEntity> categorias = new ArrayList<>();
	
	@ManyToMany(mappedBy = "listaDeseos")
	private List<UsuarioEntity> usuarios = new ArrayList<>();
}
