package com.fiap.techchallenge.pedidos.infraestruture.web.mapper;

import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;
import com.fiap.techchallenge.pedidos.domain.exceptions.CPFOuEmailObrigatorioException;
import com.fiap.techchallenge.pedidos.domain.exceptions.NomeObrigatorioException;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.ClienteEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteMapperTest {
	@Test
	void shouldThrowExceptionWhenNullValuesWasInformed() {
		ClienteEntity clienteEntity = ClienteEntity.builder()
				.build();
		var messagemException = "CPF ou e-mail é obrigatório";
		var exception1 = assertThrows(CPFOuEmailObrigatorioException.class, () -> {
			ClienteMapper.toDomain(clienteEntity);
		});
		assertEquals(messagemException, exception1.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenNomeisEmpty() {
		var messagemException = "Nome não pode ser nulo ou vazio";
		var exception1 = assertThrows(NomeObrigatorioException.class, () -> {
			ClienteDTO dto = ClienteDTO.builder()
					.build();
			ClienteMapper.toDomain(dto);
		});
		assertEquals(messagemException, exception1.getMessage());

		var exception2 = assertThrows(NomeObrigatorioException.class, () -> {
			ClienteDTO dto = ClienteDTO.builder()
					.nome("")
					.build();
			ClienteMapper.toDomain(dto);
		});
		assertEquals(messagemException, exception2.getMessage());
	}

	@Test
	void shouldReturnValidClienteWhenNotFillTheProperties(){
		ClienteDTO dto = ClienteDTO.builder()
				.nome("Elão dos Foguete")
				.build();
		assertDoesNotThrow(() -> {
			ClienteMapper.toDomain(dto);
		});
	}

	@Test
	void shouldReturnValidClienteWhenPropertiesWasFilled(){
		ClienteDTO dto = ClienteDTO.builder()
				.nome("Elão dos Foguete")
				.cpf("63656155070")
				.email("meu@mail.com")
				.build();
		assertDoesNotThrow(() -> {
			ClienteMapper.toDomain(dto);
		});
	}
}