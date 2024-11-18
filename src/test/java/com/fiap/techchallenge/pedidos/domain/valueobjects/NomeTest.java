package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.exceptions.NomeInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NomeTest {
	@Test
	void shouldNotThrowsExceptionWhenNewNome() {
		assertDoesNotThrow(() -> {
			new Nome("XPTO");
		});
	}

	@Test
	void shouldThrowsExceptionWhenNewNomeWithInvalidValue() {
		var exception = assertThrows(NomeInvalidoException.class, () -> {
			new Nome(null);
		});

		assertEquals("Nome inválido: null", exception.getMessage());
	}
}
