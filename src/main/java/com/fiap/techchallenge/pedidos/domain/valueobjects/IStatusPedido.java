package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.model.Pedido;

public interface IStatusPedido {
	IStatusPedido proximo(Pedido pedido);
	IStatusPedido voltar(Pedido pedido);
	IStatusPedido cancelar(Pedido pedido);
	StatusPedido getStatusAtual();
}
