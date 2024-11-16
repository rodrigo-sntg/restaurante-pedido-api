package com.fiap.techchallenge.pedidos.domain.valueobjects;

import java.util.Objects;

public class Preco {
	private final Double valor;

	public Preco(Double valor) {
		this.valor = Objects.requireNonNull(valor, "O valor do preço não pode ser nulo");
	}

	public Double getValor() {
		return valor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Preco preco = (Preco) o;
		return valor.equals(preco.valor);
	}

	@Override
	public int hashCode() {
		return Objects.hash(valor);
	}

}
