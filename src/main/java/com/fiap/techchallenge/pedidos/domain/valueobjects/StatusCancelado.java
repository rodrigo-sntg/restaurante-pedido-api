package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusMensagemException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;

public class StatusCancelado implements IStatusPedido{
	@Override
	public IStatusPedido proximo(Pedido pedido) {
		throw new StatusInvalidoException(StatusMensagemException.PEDIDO_JA_CANCELADO.getMensagem());
	}

	@Override
	public IStatusPedido voltar(Pedido pedido) {
		throw new StatusInvalidoException(StatusMensagemException.PEDIDO_JA_CANCELADO.getMensagem());
	}

	@Override
	public IStatusPedido cancelar(Pedido pedido) {
		throw new StatusInvalidoException(StatusMensagemException.PEDIDO_JA_CANCELADO.getMensagem());
	}

	@Override
	public StatusPedido getStatusAtual() {
		return StatusPedido.CANCELADO;
	}
}
