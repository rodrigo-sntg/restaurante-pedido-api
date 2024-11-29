package com.fiap.techchallenge.pedidos.infraestruture.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "itens_pedidos", uniqueConstraints = {})
public class ItemPedidoEntity {
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Long id;
	private String customizacao;

	@ManyToOne
	@JoinColumn(name = "pedidoid")
	private PedidoEntity pedido;

	private String nome;

	private Double preco;

	private String produtoCodigo;

	private String produtoNome;

}
