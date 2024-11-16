package com.fiap.techchallenge.pedidos.domain.exceptions;

public class PedidoInvalidoException extends RuntimeException {
	public PedidoInvalidoException(String mensagem) {
		super(mensagem);
	}
}
