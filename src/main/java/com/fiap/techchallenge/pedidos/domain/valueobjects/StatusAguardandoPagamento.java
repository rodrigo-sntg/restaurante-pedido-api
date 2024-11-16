package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusMensagemException;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;

public class StatusAguardandoPagamento implements IStatusPedido {
	@Override
	public IStatusPedido proximo(Pedido pedido) {
		pedido.atualizarDataAlteracao();
		return new StatusProcessandoPagamento();
	}

	@Override
	public IStatusPedido voltar(Pedido pedido) {
		throw new StatusInvalidoException(StatusMensagemException.PEDIDO_FINALIZADO_RECEBIDO_INDEVIDO.getMensagem());
	}

	@Override
	public IStatusPedido cancelar(Pedido pedido) {
		pedido.atualizarDataCancelada();
		return new StatusCancelado();
	}

	@Override
	public StatusPedido getStatusAtual() {
		return StatusPedido.AGUARDANDO_PAGAMENTO;
	}
}
