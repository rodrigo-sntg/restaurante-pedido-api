package com.fiap.techchallenge.pedidos.application.gateway;

import com.fiap.techchallenge.pedidos.application.repository.ClienteRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ClienteGatewayImplTest {
	@InjectMocks
	private ClienteGatewayImpl clienteGateway;
	@Mock
	private ClienteRepository clienteRepository;
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
	void shouldSaveClienteWhenSalvarClienteIsCalledWithValidData() {
		when(clienteGateway.salvarCliente(any(Cliente.class))).thenReturn(getCliente());
		assertDoesNotThrow(() -> {
			clienteGateway.salvarCliente(getCliente());
		});
	}

	@Test
	void shouldReturnClienteWhenBuscarClientePorIdIsCalledWithValidId() {
		when(clienteGateway.buscarClientePorId(any())).thenReturn(Optional.of(getCliente()));
		assertDoesNotThrow(() -> {
			clienteGateway.buscarClientePorId(1L);
		});
	}

	@Test
	void shouldReturnClienteWhenBuscarClientePorCpfIsCalledWithValidCpf() {
		when(clienteGateway.buscarClientePorCpf(any())).thenReturn(Optional.of(getCliente()));
		assertDoesNotThrow(() -> {
			clienteGateway.buscarClientePorCpf("12345678909");
		});
	}

	@Test
	void shouldReturnClienteWhenBuscarClientePorEmailIsCalledWithValidEmail() {
		when(clienteGateway.buscarClientePorEmail(any())).thenReturn(Optional.of(getCliente()));

		assertDoesNotThrow(() -> {
			clienteGateway.buscarClientePorEmail("meu@mail.com");
		});
	}

	@Test
	void shouldReturnClientesWhenBuscarClientesIsCalled() {
		when(clienteGateway.buscarClientes()).thenReturn(List.of(getCliente()));

		assertDoesNotThrow(() -> {
			clienteGateway.buscarClientes();
		});
	}

	private Cliente getCliente() {
		return new Cliente(new Nome("Nome"), new CPF("63656155070"), new Email("email@domain.com"));
	}
}

