package com.fiap.techchallenge.pedidos.application.gateway;

import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public interface PedidoGateway {
	Pedido salvarPedido(Pedido pedido);

	Optional<Pedido> buscarPedidoPeloCodigo(String codigo);

	Optional<List<Pedido>> buscarPedidos(StatusPedido status);

	Pedido atualizarStatus(Pedido pedido);

	void cancelarPedido(Pedido pedido);

	List<Pedido> buscarPedidosPorStatus(EnumSet<StatusPedido> status);
}
