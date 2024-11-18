package com.fiap.techchallenge.pedidos.domain.valueobjects;

import com.fiap.techchallenge.pedidos.domain.exceptions.CPFInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CPFTest {

	@Test
	void shouldValidateCPF() {
		String cpf = "054.910.980-35";
		assertDoesNotThrow(() -> {
			new CPF(cpf);
		});

	}

	@Test
	void shouldThrowExceptionWhenCPFIsInvalid() {
		String cpf = "123.456.789-00";
		var exception = assertThrows(CPFInvalidoException.class, () -> {
			new CPF(cpf);
		});
		assertEquals("CPF inválido: " + cpf, exception.getMessage());

		String cpfWrongSize = "123.456.789-000";
		var exception2 = assertThrows(CPFInvalidoException.class, () -> {
			new CPF(cpfWrongSize);
		});
		assertEquals("CPF inválido: " + cpfWrongSize, exception2.getMessage());

		String cpfRepetead = "111.111.111-11";
		var exception3 = assertThrows(CPFInvalidoException.class, () -> {
			new CPF(cpfRepetead);
		});
		assertEquals("CPF inválido: " + cpfRepetead, exception3.getMessage());
	}

}
