package com.fiap.techchallenge.pedidos.application.gateway;

import com.fiap.techchallenge.pedidos.application.repository.PedidoRepository;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class PedidoGatewayImpl implements PedidoGateway {
	private final PedidoRepository pedidoRepository;

	public PedidoGatewayImpl(PedidoRepository pedidoRepository) {
		this.pedidoRepository = pedidoRepository;
	}

	@Override
	public Pedido salvarPedido(Pedido pedido) {
		return pedidoRepository.salvarPedido(pedido);
	}

	@Override
	public Optional<Pedido> buscarPedidoPeloCodigo(String codigo) {
		return pedidoRepository.buscarPedidoPeloCodigo(codigo);
	}

	@Override
	public Optional<List<Pedido>> buscarPedidos(StatusPedido status) {
		return pedidoRepository.buscarPedidos(status);
	}

	@Override
	public Pedido atualizarStatus(Pedido pedido) {
		return pedidoRepository.atualizarStatus(pedido);
	}

	@Override
	public void cancelarPedido(Pedido pedido) {
		pedidoRepository.cancelarPedido(pedido);
	}

	@Override
	public List<Pedido> buscarPedidosPorStatus(EnumSet<StatusPedido> status) {
		return pedidoRepository.buscarPedidosPorStatus(status);
	}
}
