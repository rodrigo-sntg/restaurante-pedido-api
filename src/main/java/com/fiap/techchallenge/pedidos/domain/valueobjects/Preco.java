package com.fiap.techchallenge.pedidos.domain.valueobjects;

import lombok.EqualsAndHashCode;

import java.util.Objects;

@EqualsAndHashCode
public class Preco {
	private final Double valor;

	public Preco(Double valor) {
		this.valor = Objects.requireNonNull(valor, "O valor do preço não pode ser nulo");
	}

	public Double getValor() {
		return valor;
	}
}
