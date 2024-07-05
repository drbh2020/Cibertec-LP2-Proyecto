package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_usuario")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {
	
	@Id
	private String correo;
	private String password;
	private String nombre;
	private String apellidos;
	
	@ManyToOne
	@JoinColumn(name = "tipo_id_tipo", nullable = false)
	private TipoEntity tipo;
	
	private String urlImagen;	
}