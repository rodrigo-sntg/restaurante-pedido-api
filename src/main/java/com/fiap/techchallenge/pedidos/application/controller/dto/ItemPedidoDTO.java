package com.fiap.techchallenge.pedidos.application.controller.dto;

import com.fiap.techchallenge.pedidos.application.presenters.ModelView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemPedidoDTO implements ModelView {
	private String customizacao;
	private String nomeProduto;
	private Double preco;
	private String descricao;
}
