package com.fiap.techchallenge.pedidos.application.controller;

import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.PedidoDTO;
import com.fiap.techchallenge.pedidos.application.presenters.ApplicationPresenter;
import com.fiap.techchallenge.pedidos.application.usecase.PedidoUseCase;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class PedidoControllerImpl implements PedidoController {
	private final PedidoUseCase pedidoUseCase;
	private final ApplicationPresenter<PedidoDTO> pedidoPresenter;

	public PedidoControllerImpl(PedidoUseCase pedidoUseCase, //
			ApplicationPresenter<PedidoDTO> pedidoPresenter) {
		this.pedidoUseCase = pedidoUseCase;
		this.pedidoPresenter = pedidoPresenter;
	}

	@Override
	public PedidoDTO criarPedido(CadastroPedidoDTO cadastroPedidoDTO) {
		var pedidoCriado = pedidoUseCase.criarPedido(cadastroPedidoDTO);
		return this.pedidoPresenter.toModelView(pedidoCriado);
	}

	@Override
	public Optional<PedidoDTO> buscarPedidoPeloCodigo(String codigo) {
		Optional<Pedido> optionalPedido = pedidoUseCase.buscarPedidoPeloCodigo(codigo);
		return optionalPedido.map(this.pedidoPresenter::toModelView);
	}

	@Override
	public List<PedidoDTO> buscarPedidos(String status) {
		StatusPedido statusPedido = null;
		if (!isBlank(status)) {
			statusPedido = toStatusPedido(status).orElseThrow(
					() -> new StatusInvalidoException("O status informado não existe!"));
		}
		var pedidosOptional = pedidoUseCase.buscarPedidos(statusPedido);
		if (pedidosOptional.isEmpty()) {
			return new ArrayList<>();
		}
		List<Pedido> pedidos = pedidosOptional.get();
		return pedidos.stream()
				.map(this.pedidoPresenter::toModelView)
				.toList();
	}

	@Override
	public List<PedidoDTO> buscarPedidosEmProgresso() {
		EnumSet<StatusPedido> statusEmProgresso = EnumSet.of(StatusPedido.RECEBIDO, StatusPedido.EM_ELABORACAO,
				StatusPedido.PRONTO);
		List<Pedido> pedidos = pedidoUseCase.buscarPedidosPorStatus(statusEmProgresso);
		return pedidos.stream()
				.map(this.pedidoPresenter::toModelView)
				.toList();
	}

	@Override
	public void cancelarPedido(String codigo) {
		Pedido pedido = pedidoUseCase.buscarPedidoPeloCodigo(codigo)
				.orElseThrow(() -> new PedidoInvalidoException("Pedido não localizado"));
		pedidoUseCase.cancelarPedido(pedido);
	}

	@Override
	public PedidoDTO atualizarStatusPedidoPago(String codigo) {
		var pedido = pedidoUseCase.avancarStatus(codigo);
		return this.pedidoPresenter.toModelView(pedido);
	}

//	@Override
//	public boolean atualizarStatusPedidoPago(OrderResponseInputData orderResponse) {
//
//		return orderResponse.getChargeDTOS()
//				.stream()
//				.filter(chargeDTO -> ChargeInputData.STATUS_PAID.equals(chargeDTO.getStatus()))
//				.findFirst()
//				.map(chargeDTO -> {
//					pedidoUseCase.avancarStatus(orderResponse.getReferenceId());
//					return true;
//				})
//				.orElse(false);
//
//	}

	@Override
	public PedidoDTO voltarStatus(String codigo) {
		var pedido = pedidoUseCase.voltarStatus(codigo);
		return this.pedidoPresenter.toModelView(pedido);
	}

	private boolean isBlank(String str) {
		return str == null || str.trim()
				.isEmpty();
	}

	private Optional<StatusPedido> toStatusPedido(String status) {
		return Arrays.stream(StatusPedido.values())
				.filter(item -> item.name()
						.equalsIgnoreCase(status))
				.findFirst();
	}
}
