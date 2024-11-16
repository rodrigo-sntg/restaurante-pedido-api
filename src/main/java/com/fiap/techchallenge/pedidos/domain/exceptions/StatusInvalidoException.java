package com.fiap.techchallenge.pedidos.domain.exceptions;

public class StatusInvalidoException extends RuntimeException {
	public StatusInvalidoException(String mensagem) {
		super(mensagem);
	}
}
