package com.urbanhop.demo.entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "detalle_pedido")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedidoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long detalleId;
	
	private Integer cantidad;
	
	@ManyToOne
	@JoinColumn(name = "producto_id", nullable = false)
	private ProductoEntity productoEntity;
	
	@ManyToOne
	@JoinColumn(name = "pedido_id", nullable = false)
	private PedidoEntity pedidoEntity;

}
