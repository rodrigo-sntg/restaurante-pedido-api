package com.fiap.techchallenge.pedidos.application.controller;

import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.PedidoDTO;

import java.util.List;
import java.util.Optional;

public interface PedidoController {
	PedidoDTO criarPedido(CadastroPedidoDTO cadastroPedidoDTO);

	Optional<PedidoDTO> buscarPedidoPeloCodigo(String codigo);

	List<PedidoDTO> buscarPedidos(String status);

	List<PedidoDTO> buscarPedidosEmProgresso();

	void cancelarPedido(String codigo);

	PedidoDTO atualizarStatusPedidoPago(String codigo);

//	boolean atualizarStatusPedidoPago(OrderResponseInputData orderResponse);

	PedidoDTO voltarStatus(String codigo);
}
