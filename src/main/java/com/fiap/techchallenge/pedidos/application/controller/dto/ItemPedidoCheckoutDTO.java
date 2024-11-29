package com.fiap.techchallenge.pedidos.application.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemPedidoCheckoutDTO {
	private String customizacao;
	private int preco; //preço em centavos
	private String nome;
	private String codigoProduto;
}
