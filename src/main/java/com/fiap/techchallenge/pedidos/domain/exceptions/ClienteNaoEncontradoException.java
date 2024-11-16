package com.fiap.techchallenge.pedidos.domain.exceptions;

public class ClienteNaoEncontradoException extends RuntimeException {
	public ClienteNaoEncontradoException(String message) {
		super(message);
	}
}
