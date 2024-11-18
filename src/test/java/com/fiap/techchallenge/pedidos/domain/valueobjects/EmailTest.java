package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.exceptions.EmailInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {
	@Test
	void shouldNotThrowsExceptionWhenEmailIsValid() {
		assertDoesNotThrow(() -> {
			new Email("mei@email.com.br");
		});
	}

	@Test
	void shouldThrowsExceptionWhenEmailIsInvalid() {
		String email = null;
		var exception = assertThrows(EmailInvalidoException.class, () -> {
			new Email(email);
		});
		assertEquals("Endereço de email inválido: " + email, exception.getMessage());
	}
}
