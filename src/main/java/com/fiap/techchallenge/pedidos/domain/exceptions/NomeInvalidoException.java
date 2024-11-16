package com.fiap.techchallenge.pedidos.domain.exceptions;

public class NomeInvalidoException extends RuntimeException{
	public NomeInvalidoException(String message) {
		super(message);
	}
}
