package com.fiap.techchallenge.pedidos.domain.exceptions;

public class CPFInvalidoException extends RuntimeException {
	public CPFInvalidoException(String message) {
		super(message);
	}
}
