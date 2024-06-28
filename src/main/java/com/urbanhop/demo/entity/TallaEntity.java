package com.urbanhop.demo.entity;


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
@Table(name = "tb_talla")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TallaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_talla")
	private Integer tallaId;
	
	@Column(name = "nombre", columnDefinition = "VARCHAR(3)")
	private String nombre;
}
