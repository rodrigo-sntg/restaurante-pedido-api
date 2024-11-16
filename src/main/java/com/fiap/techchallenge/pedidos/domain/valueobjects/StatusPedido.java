package com.fiap.techchallenge.pedidos.domain.valueobjects;

import lombok.Getter;

@Getter
public enum StatusPedido {
	AGUARDANDO_PAGAMENTO(new StatusAguardandoPagamento()),
	PROCESSANDO_PAGAMENTO(new StatusProcessandoPagamento()),
	RECEBIDO(new StatusRecebido()),
	EM_ELABORACAO(new StatusEmElaboracao()),
	PRONTO(new StatusPronto()),
	FINALIZADO(new StatusFinalizado()),
	CANCELADO(new StatusCancelado());

	private final IStatusPedido statusAtual;
	private StatusPedido(IStatusPedido statusAtual) {
		this.statusAtual = statusAtual;
	}
}
