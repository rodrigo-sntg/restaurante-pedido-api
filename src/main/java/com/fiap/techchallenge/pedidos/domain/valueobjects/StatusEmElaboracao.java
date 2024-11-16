package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.model.Pedido;

public class StatusEmElaboracao implements IStatusPedido {
	@Override
	public IStatusPedido proximo(Pedido pedido) {
		pedido.atualizarDataAlteracao();
		return new StatusPronto();
	}

	@Override
	public IStatusPedido voltar(Pedido pedido) {
		pedido.atualizarDataAlteracao();
		return new StatusRecebido();
	}

	@Override
	public IStatusPedido cancelar(Pedido pedido) {
		pedido.atualizarDataCancelada();
		return new StatusCancelado();
	}

	@Override
	public StatusPedido getStatusAtual() {
		return StatusPedido.EM_ELABORACAO;
	}
}
