package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusMensagemException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;

public class StatusRecebido implements IStatusPedido {
	@Override
	public IStatusPedido proximo(Pedido pedido) {
		pedido.atualizarDataAlteracao();
		return new StatusEmElaboracao();
	}

	@Override
	public IStatusPedido voltar(Pedido pedido) {
		pedido.atualizarDataAlteracao();
		throw new StatusInvalidoException(StatusMensagemException.PEDIDO_RECEBIDO_ALTERACAO_INDEVIDA.getMensagem());
	}

	@Override
	public IStatusPedido cancelar(Pedido pedido) {
		pedido.atualizarDataCancelada();
		return new StatusCancelado();
	}

	@Override
	public StatusPedido getStatusAtual() {
		return StatusPedido.RECEBIDO;
	}
}
