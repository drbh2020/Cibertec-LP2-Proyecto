package com.urbanhop.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	private Integer productoId;
	
	@Column(name = "nombre", length = 45, nullable = false)
	private String nombre;
	
	@Column(name = "stock", nullable = false)
	private Integer stock;
	
	@Column(name = "precio", nullable = false)
	private Double precio;
		
	@Column(name = "estado", nullable = false)
	private Boolean estado;
	
	@ManyToOne
	@JoinColumn(name = "id_talla", nullable = false)
	private TallaEntity tallaEntity;

	@ManyToOne
	@JoinColumn(name = "id_categoria", nullable = false)
	private CategoriaEntity categoriaEntity;
	
	private String urlImagen;
}
