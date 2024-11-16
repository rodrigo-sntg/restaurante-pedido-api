package com.fiap.techchallenge.pedidos.domain.exceptions;

public class CPFOuEmailObrigatorioException extends RuntimeException {
	public CPFOuEmailObrigatorioException(String message) {
		super(message);
	}
}
