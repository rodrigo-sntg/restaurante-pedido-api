package com.fiap.techchallenge.pedidos.domain.exceptions;

public class EmailInvalidoException extends RuntimeException {
	public EmailInvalidoException(String message) {
		super(message);
	}
}
