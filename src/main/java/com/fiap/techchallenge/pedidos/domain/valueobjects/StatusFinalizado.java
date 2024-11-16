package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusMensagemException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;

public class StatusFinalizado implements IStatusPedido {
	@Override
	public IStatusPedido proximo(Pedido pedido) {
		throw new StatusInvalidoException(StatusMensagemException.PEDIDO_JA_FINALIZADO.getMensagem());
	}

	@Override
	public IStatusPedido voltar(Pedido pedido) {
		throw new StatusInvalidoException(StatusMensagemException.PEDIDO_FINALIZADO_STATUS_INDEVIDO.getMensagem());
	}

	@Override
	public IStatusPedido cancelar(Pedido pedido) {
		pedido.atualizarDataCancelada();
		return new StatusCancelado();
	}

	@Override
	public StatusPedido getStatusAtual() {
		return StatusPedido.FINALIZADO;
	}
}
