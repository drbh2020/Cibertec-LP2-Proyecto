package com.urbanhop.demo.entity;

import jakarta.persistence.JoinColumn; 
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.util.ArrayList;
import java.util.List;

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
@Table(name = "tb_usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UsuarioEntity {
	
	@Id
	@Column(name = "correo", length = 45, nullable = false)
	private String correo;

	@Column(name = "password", length = 255, nullable = false)
	private String password;
	
	@Column(name = "nombre", length = 45, nullable = false)
	private String nombre;
	
	@Column(name = "apellido", length = 45, nullable = false)
	private String apellido; // apellidos en el otro
	
	@Column(name = "estado", nullable = false, columnDefinition = "bit(1) default 1")
	private Boolean estado;
	
	@ManyToOne
	@JoinColumn(name = "tipo_id_tipo", nullable = false)
	private TipoEntity tipo;

}
