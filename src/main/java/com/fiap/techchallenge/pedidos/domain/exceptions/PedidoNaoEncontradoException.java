package com.fiap.techchallenge.pedidos.domain.exceptions;

public class PedidoNaoEncontradoException extends RuntimeException {
	public PedidoNaoEncontradoException(String message) {
		super(message);
	}
}
