package com.fiap.techchallenge.pedidos.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrecoTest {

	@Test
	void shouldValidatePreco()  {
		Double valor = 10.0;
		Preco preco = new Preco(valor);
		assertEquals(valor, preco.getValor());
	}

	@Test
	void shouldThrowsWhenPrecoIsNull() {
		var exception = assertThrows(NullPointerException.class, () -> {
			new Preco(null);
		});
		assertEquals("O valor do preço não pode ser nulo", exception.getMessage());
	}
}
