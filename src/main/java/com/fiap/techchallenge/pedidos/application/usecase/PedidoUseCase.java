package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.CheckoutDTO;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public interface PedidoUseCase {
	Pedido criarPedido(CadastroPedidoDTO inputData);

	Optional<Pedido> buscarPedidoPeloCodigo(String codigo);

	Optional<List<Pedido>> buscarPedidos(StatusPedido status);

	void cancelarPedido(Pedido pedido);

	Pedido avancarStatus(String codigo);

	Pedido voltarStatus(String codigo);

	void verificarEAtualizarStatusDosPedidos(LocalDateTime agora);

	List<Pedido> buscarPedidosPorStatus(EnumSet<StatusPedido> status);

	CheckoutDTO checkout(String codigo);
}
