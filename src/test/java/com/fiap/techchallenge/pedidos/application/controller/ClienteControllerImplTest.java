package com.fiap.techchallenge.pedidos.application.controller;

import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;
import com.fiap.techchallenge.pedidos.application.presenters.ClientePresenter;
import com.fiap.techchallenge.pedidos.application.usecase.ClienteUseCase;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.valueobjects.CPF;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Email;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Nome;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ClienteControllerImplTest {
	@InjectMocks
	private ClienteControllerImpl clienteController;
	@Mock
	private ClienteUseCase clienteUseCase;
	@Mock
	private ClientePresenter clientePresenter;

	private AutoCloseable openMocks;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void shouldReturnClienteWhenBuscarClienteIsCalled() {
		var cliente = new Cliente(new Nome("Nome"), new CPF("12345678909"), new Email("meu@teste.com.br"));
		var clienteDTO = ClienteDTO.builder()
				.nome("Nome")
				.cpf("12345678909")
				.email("nome@teste.com.br")
				.build();
		when(clienteUseCase.buscarClientes()).thenReturn(List.of(cliente));
		when(clientePresenter.toModelView(any(Object.class))).thenReturn(clienteDTO);
		List<ClienteDTO> clienteDTOS = clienteController.buscarClientes();
		assertEquals(clienteDTOS.iterator()
				.next()
				.getNome(), cliente.getNome()
				.getValor());
	}

	@Test
	void shouldCreateClienteWhenCriarClienteIsCalled() {
		var cliente = new Cliente(new Nome("Nome"), new CPF("12345678909"), new Email("meu@teste.com.br"));
		var clienteDTO = ClienteDTO.builder()
				.nome("Nome")
				.cpf("12345678909")
				.email("nome@teste.com.br")
				.build();
		when(clienteUseCase.criarCliente(any(Cliente.class))).thenReturn(cliente);
		when(clientePresenter.toModelView(any(Object.class))).thenReturn(clienteDTO);

		ClienteDTO clienteCriado = clienteController.criarCliente(clienteDTO);
		assertEquals(clienteCriado.getNome(), cliente.getNome()
				.getValor());
	}

}
