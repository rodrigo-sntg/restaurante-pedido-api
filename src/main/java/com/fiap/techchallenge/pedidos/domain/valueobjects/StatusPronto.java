package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.model.Pedido;

public class StatusPronto implements IStatusPedido{
	@Override
	public IStatusPedido proximo(Pedido pedido) {
		pedido.atualizarDataAlteracao();
		return new StatusFinalizado();
	}

	@Override
	public IStatusPedido voltar(Pedido pedido) {
		pedido.atualizarDataAlteracao();
		return new StatusEmElaboracao();
	}

	@Override
	public IStatusPedido cancelar(Pedido pedido) {
		pedido.atualizarDataCancelada();
		return new StatusCancelado();
	}

	@Override
	public StatusPedido getStatusAtual() {
		return StatusPedido.PRONTO;
	}
}
