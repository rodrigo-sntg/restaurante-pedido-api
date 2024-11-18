package com.fiap.techchallenge.pedidos.infraestruture.persistence.repositories;

import com.fiap.techchallenge.pedidos.domain.exceptions.CPFOuEmailObrigatorioException;
import com.fiap.techchallenge.pedidos.domain.exceptions.NomeObrigatorioException;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.valueobjects.CPF;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Email;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Nome;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.ClienteEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ClienteRepositoryImplTest {
	@InjectMocks
	private ClienteRepositoryImpl repository;
	@Mock
	private ClienteJpaRepository jpaRepository;
	private AutoCloseable openMocks;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Nested
	class SalvarCliente {
		@Test
		void shouldPersistCliente_WhenSalvarClienteIsCalled_WithValidData() {
			ClienteEntity entity = new ClienteEntity();
			entity.setNome("Nome");
			entity.setCpf("63656155070");
			when(jpaRepository.save(any(ClienteEntity.class))).thenReturn(entity);

			Cliente cliente = new Cliente(new Nome("Nome"), new CPF("63656155070"), new Email("email@domain.com"));
			when(jpaRepository.findById(anyLong())).thenReturn(Optional.of(entity));

			Cliente savedCliente = repository.salvarCliente(cliente);
			assertNotNull(savedCliente);
			assertEquals(new Nome("Nome"), savedCliente.getNome());
		}

		@Test
		void shouldThrowException_WhenSalvarClienteIsCalled_AndRepositoryThrowsException() {
			Cliente cliente = new Cliente(new Nome("Nome"), new CPF("63656155070"), new Email("email@domain.com"));
			when(jpaRepository.save(any(ClienteEntity.class))).thenThrow(new DataIntegrityViolationException("Conflito de integridade de dados"));

			assertThrows(DataIntegrityViolationException.class, () -> {
				repository.salvarCliente(cliente);
			});
		}

		@Test
		void shouldThrowException_WhenSalvarClienteIsCalled_WithNoEmailAndNoCPFIsPassed() {
			Cliente cliente = new Cliente(new Nome("Nome"), null, null);
			when(jpaRepository.save(any(ClienteEntity.class))).thenThrow(new CPFOuEmailObrigatorioException("CPF ou e-mail é obrigatório"));

			assertThrows(CPFOuEmailObrigatorioException.class, () -> {
				repository.salvarCliente(cliente);
			});
		}

		@Test
		void shouldThrowException_WhenSalvarClienteIsCalled_NoPassed() {

			assertThrows(NomeObrigatorioException.class, () -> {
				new Cliente(null, null, null);
			});
		}
	}

	@Nested
	class BuscarCliente {
		@Test
		void shouldReturnCliente_WhenBuscarClienteIsCalled_WithValidId() {
			ClienteEntity entity = new ClienteEntity();
			entity.setNome("Nome");
			entity.setCpf("63656155070");
			when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

			Optional<Cliente> foundCliente = repository.buscarClientePorId(1L);
			assertTrue(foundCliente.isPresent());
			assertEquals("Nome", foundCliente.get().getNome().getValor());
			assertEquals("63656155070", foundCliente.get().getCpf().getNumero());
		}

		@Test
		void shouldReturnEmpty_WhenBuscarClienteIsCalled_WithNonexistentId() {
			when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

			Optional<Cliente> foundCliente = repository.buscarClientePorId(99L);
			assertFalse(foundCliente.isPresent());
		}

		@Test
		void shouldThrowException_WhenBuscarClienteIsCalled_AndDatabaseFails() {
			when(jpaRepository.findById(anyLong())).thenThrow(new DataAccessResourceFailureException("Falha de acesso ao banco de dados"));

			assertThrows(DataAccessResourceFailureException.class, () -> {
				repository.buscarClientePorId(1L);
			});
		}
	}

	@Nested
	class BuscarClientePorCpf {
		@Test
		void shouldReturnCliente_WhenBuscarClientePorCpfIsCalled_WithValidCpf() {
			ClienteEntity entity = new ClienteEntity();
			entity.setNome("Nome");
			entity.setCpf("12345678909");
			when(jpaRepository.findByCpf("12345678909")).thenReturn(Optional.of(entity));

			Optional<Cliente> foundCliente = repository.buscarClientePorCpf("12345678909");
			assertTrue(foundCliente.isPresent());
			assertEquals("Nome", foundCliente.get().getNome().getValor());
			assertEquals("12345678909", foundCliente.get().getCpf().getNumero());
		}

		@Test
		void shouldReturnEmpty_WhenBuscarClientePorCpfIsCalled_WithNonexistentCpf() {
			when(jpaRepository.findByCpf("invalido")).thenReturn(Optional.empty());

			Optional<Cliente> foundCliente = repository.buscarClientePorCpf("invalido");
			assertFalse(foundCliente.isPresent());
		}
	}

	@Nested
	class BuscarClientePorEmail {
		@Test
		void shouldReturnCliente_WhenBuscarClientePorEmailIsCalled_WithValidEmail() {
			ClienteEntity entity = new ClienteEntity();
			entity.setNome("Nome");
			entity.setEmail("email@domain.com");
			when(jpaRepository.findByEmail("email@domain.com")).thenReturn(Optional.of(entity));

			Optional<Cliente> foundCliente = repository.buscarClientePorEmail("email@domain.com");
			assertTrue(foundCliente.isPresent());
			assertEquals("Nome", foundCliente.get().getNome().getValor());
			assertEquals("email@domain.com", foundCliente.get().getEmail().getEndereco());
		}

		@Test
		void shouldReturnEmpty_WhenBuscarClientePorEmailIsCalled_WithNonexistentEmail() {
			when(jpaRepository.findByEmail("notfound@domain.com")).thenReturn(Optional.empty());

			Optional<Cliente> foundCliente = repository.buscarClientePorEmail("notfound@domain.com");
			assertFalse(foundCliente.isPresent());
		}
	}

	@Nested
	class BuscarClientes {
		@Test
		void shouldReturnAllClientes_WhenBuscarClientesIsCalled() {
			List<ClienteEntity> clienteEntities = Stream.of(
					new ClienteEntity(1L, "NomeUm", "23229458036", "email1@domain.com"),
					new ClienteEntity(2L, "NomeDois", "23229458036", "email2@domain.com")
			).collect(Collectors.toList());
			when(jpaRepository.findAll()).thenReturn(clienteEntities);

			List<Cliente> foundClientes = repository.buscarClientes();
			assertNotNull(foundClientes);
			assertEquals(2, foundClientes.size());
			assertEquals("NomeUm", foundClientes.get(0).getNome().getValor());
		}

		@Test
		void shouldReturnEmptyList_WhenNoClientesAreFound() {
			when(jpaRepository.findAll()).thenReturn(List.of());

			List<Cliente> foundClientes = repository.buscarClientes();
			assertTrue(foundClientes.isEmpty());
		}
	}
}
