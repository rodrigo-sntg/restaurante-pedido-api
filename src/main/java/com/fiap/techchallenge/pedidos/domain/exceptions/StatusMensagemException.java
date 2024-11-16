package com.fiap.techchallenge.pedidos.domain.exceptions;

import lombok.Getter;

@Getter
public enum StatusMensagemException {
	PEDIDO_JA_CANCELADO("O Pedido já está com o status cancelado!"),
	PEDIDO_JA_FINALIZADO("O Pedido já está com o status finalizado!"),
	PEDIDO_FINALIZADO_STATUS_INDEVIDO("O Pedido já está com o status finalizado e não pode ter status alterado!"),
	PEDIDO_FINALIZADO_RECEBIDO_INDEVIDO("O pedido está com o status recebido, não é possivel voltar para status anterior!"),
	PEDIDO_FINALIZADO_CANCELAMENTO_IMPOSSIVEL("O Pedido já está finalizado e não pode ser cancelado!"),
	PEDIDO_RECEBIDO_ALTERACAO_INDEVIDA("O pedido já foi recebido e não deve voltar o status!");
	private final String mensagem;

	private StatusMensagemException(String mensagem) {
		this.mensagem = mensagem;
	}
}
