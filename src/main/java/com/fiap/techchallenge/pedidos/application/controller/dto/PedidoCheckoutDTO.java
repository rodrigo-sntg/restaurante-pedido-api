package com.fiap.techchallenge.pedidos.application.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoCheckoutDTO {
	private List<ItemPedidoCheckoutDTO> itens;
	private String codigoPedido;
}
