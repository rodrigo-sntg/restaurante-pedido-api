package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.application.gateway.ClienteGateway;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.valueobjects.CPF;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Email;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Nome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClienteUseCaseImplTest {
	private ClienteUseCaseImpl service;
	private ClienteGateway gateway;

	@BeforeEach
	void setup() {
		gateway = mock(ClienteGateway.class);
		service = new ClienteUseCaseImpl(gateway);
	}

	@Nested
	class CriarCliente {
		@Test
		void shouldSaveClienteWhenCriarClienteIsCalledWithValidData() throws Exception {
			Cliente cliente = new Cliente(new Nome("Nome"), new CPF("12345678909"), new Email("email@domain.com"));
			when(gateway.salvarCliente(any(Cliente.class))).thenReturn(cliente);

			Cliente savedCliente = service.criarCliente(cliente);
			assertNotNull(savedCliente);
			assertEquals(new Nome("Nome"), savedCliente.getNome());
		}

		@Test
		void shouldThrowExceptionWhenCriarClienteIsCalledWithInvalidData() {
			Cliente cliente = new Cliente(new Nome("Nome"), new CPF("12345678909"), new Email("email@domain.com"));
			when(gateway.salvarCliente(any(Cliente.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

			assertThrows(IllegalArgumentException.class, () -> {
				service.criarCliente(cliente);
			});
		}
	}

	@Nested
	class BuscarCliente {
		@Test
		void shouldReturnClienteWhenBuscarClienteIsCalledWithValidId() {
			Optional<Cliente> cliente = Optional.of(new Cliente(new Nome("Nome"), new CPF("12345678909"), new Email("email@domain.com")));
			when(gateway.buscarClientePorId(1L)).thenReturn(cliente);

			Optional<Cliente> foundCliente = service.buscarClientePorId(1L);
			assertTrue(foundCliente.isPresent());
			assertEquals(new Nome("Nome"), foundCliente.get().getNome());
		}

		@Test
		void shouldReturnEmptyWhenBuscarClienteIsCalledWithNonexistentId() {
			when(gateway.buscarClientePorId(99L)).thenReturn(Optional.empty());

			Optional<Cliente> foundCliente = service.buscarClientePorId(99L);
			assertFalse(foundCliente.isPresent());
		}

		@Test
		void shouldThrowExceptionWhenBuscarClienteIsCalledAndRepositoryThrows() {
			when(gateway.buscarClientePorId(anyLong())).thenThrow(new RuntimeException("Erro de acesso ao banco de dados"));

			assertThrows(RuntimeException.class, () -> {
				service.buscarClientePorId(1L);
			});
		}

		@Test
		void shouldReturnClienteWhenBuscarClienteIsCalledWithValidCpf() {
			Optional<Cliente> cliente = Optional.of(new Cliente(new Nome("Nome"), new CPF("12345678909"), new Email("email@domain.com")));
			when(gateway.buscarClientePorCpf("12345678909")).thenReturn(cliente);

			Optional<Cliente> foundCliente = service.buscarClientePorCpf("12345678909");
			assertTrue(foundCliente.isPresent());
			assertEquals("email@domain.com", foundCliente.get().getEmail().getEndereco());
		}

		@Test
		void shouldReturnEmptyWhenBuscarClienteIsCalledWithInvalidCpf() {
			when(gateway.buscarClientePorCpf("invalido")).thenReturn(Optional.empty());

			Optional<Cliente> foundCliente = service.buscarClientePorCpf("invalido");
			assertFalse(foundCliente.isPresent());
		}

		@Test
		void shouldReturnClienteWhenBuscarClienteIsCalledWithValidEmail() {
			Optional<Cliente> cliente = Optional.of(new Cliente(new Nome("Nome"), new CPF("12345678909"), new Email("email@domain.com")));
			when(gateway.buscarClientePorEmail("email@domain.com")).thenReturn(cliente);

			Optional<Cliente> foundCliente = service.buscarClientePorEmail("email@domain.com");
			assertTrue(foundCliente.isPresent());
			assertEquals("Nome", foundCliente.get().getNome().getValor());
		}

		@Test
		void shouldReturnEmptyWhenBuscarClienteIsCalledWithNonexistentEmail() {
			when(gateway.buscarClientePorEmail("notfound@domain.com")).thenReturn(Optional.empty());

			Optional<Cliente> foundCliente = service.buscarClientePorEmail("notfound@domain.com");
			assertFalse(foundCliente.isPresent());
		}
	}

	@Nested
	class BuscarTodosClientes {
		@Test
		void shouldReturnAllClientesWhenBuscarClientesIsCalled() {
			List<Cliente> clientes = List.of(
					new Cliente(new Nome("NomeUm"), new CPF("23229458036"), new Email("email1@domain.com")),
					new Cliente(new Nome("NomeDois"), new CPF("23229458036"), new Email("email2@domain.com"))
			);
			when(gateway.buscarClientes()).thenReturn(clientes);

			List<Cliente> foundClientes = service.buscarClientes();
			assertNotNull(foundClientes);
			assertEquals(2, foundClientes.size());
			assertEquals("NomeUm", foundClientes.get(0).getNome().getValor());
		}

		@Test
		void shouldReturnEmptyListWhenNoClientesAreFound() {
			when(gateway.buscarClientes()).thenReturn(List.of());

			List<Cliente> foundClientes = service.buscarClientes();
			assertNotNull(foundClientes);
			assertTrue(foundClientes.isEmpty());
		}


		@Test
		void shouldHandleLargeNumberOfClientes() {
			List<Cliente> clientes = mockLargeNumberOfClientes(100); // Simulating 100 clients
			when(gateway.buscarClientes()).thenReturn(clientes);

			List<Cliente> foundClientes = service.buscarClientes();
			assertEquals(100, foundClientes.size());
		}

		@Test
		void shouldVerifyCorrectOrderIfRequired() {
			List<Cliente> clientes = List.of(
					new Cliente(new Nome("Zeta"), new CPF("23229458036"), new Email("zeta@domain.com")),
					new Cliente(new Nome("Alpha"), new CPF("23229458036"), new Email("alpha@domain.com"))
			);
			when(gateway.buscarClientes()).thenReturn(clientes);

			List<Cliente> foundClientes = service.buscarClientes();
			assertEquals("Alpha", foundClientes.get(1).getNome().getValor()); // Assuming list should be sorted by Nome
		}

		@Test
		void shouldHandleRepositoryExceptions() {
			when(gateway.buscarClientes()).thenThrow(new RuntimeException("Database is down"));

			Exception exception = assertThrows(RuntimeException.class, () -> service.buscarClientes());
			assertEquals("Database is down", exception.getMessage());
		}
	}

	private List<Cliente> mockLargeNumberOfClientes(int count) {
		return Stream.generate(() -> new Cliente(new Nome("Test"), new CPF("12345678909"), new Email("test@domain.com")))
				.limit(count)
				.collect(Collectors.toList());
	}
}
