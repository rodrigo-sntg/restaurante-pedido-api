package com.fiap.techchallenge.pedidos.infraestruture.persistence.repositories;

import com.fiap.techchallenge.pedidos.domain.exceptions.ClienteNaoEncontradoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoNaoEncontradoException;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.model.ItemPedido;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.CPF;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Nome;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Preco;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusRecebido;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.ClienteEntity;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.ItemPedidoEntity;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.PedidoEntity;
import com.fiap.techchallenge.pedidos.infraestruture.web.mapper.PedidoMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PedidoRepositoryImplTest {
	@Mock
	private ItemPedidoJpaRepository itemPedidoJpaRepository;
	@Mock
	private ClienteJpaRepository clienteJpaRepository;
	@Mock
	private PedidoMapper pedidoMapper;
	@Mock
	private PedidoJpaRepository pedidoJpaRepository;
	@InjectMocks
	private PedidoRepositoryImpl pedidoRepository;
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
	class SalvarPedido {
		@Test
		void shouldPersistPedido_WhenSalvarPedidoIsCalled_WithValidData() {
			var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10);
			var pedido = Pedido.builder()
					.itens(List.of(item))
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.statusPedido(new StatusRecebido())
					.build();

			var itemPedidoEntity = ItemPedidoEntity.builder()
					.nome("X-Salada")
					.preco(24d)
					.produtoCodigo("XPTO")
					.build();
			var pedidoEntity = PedidoEntity.builder()
					.id(1L)
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.dataAlteracao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.itens(List.of(itemPedidoEntity))
					.codigoPedido("PD1")
					.status(StatusPedido.RECEBIDO)
					.total(24d)
					.build();

			when(pedidoMapper.toEntity(any())).thenReturn(pedidoEntity);
			when(itemPedidoJpaRepository.saveAll(anyList())).thenReturn(List.of(itemPedidoEntity));
			when(pedidoJpaRepository.save(any())).thenReturn(pedidoEntity);
			when(pedidoMapper.toDomain(any(PedidoEntity.class))).thenReturn(pedido);
			Pedido pedidoSalvo = pedidoRepository.salvarPedido(pedido);
			verify(itemPedidoJpaRepository).saveAll(anyList());
			verify(pedidoJpaRepository).save(any());
			assertEquals(pedidoSalvo.getStatusPedido()
					.getStatusAtual(), pedidoEntity.getStatus());
		}

		@Test
		void shouldPersistPedido_WhenSalvarPedidoIsCalled_WithValidDataUsingCliente() {
			var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10);
			var pedido = Pedido.builder()
					.itens(List.of(item))
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.cliente(new Cliente(new Nome("Elão dos Foguete"), new CPF("63656155070"), null))
					.statusPedido(new StatusRecebido())
					.build();

			var itemPedidoEntity = ItemPedidoEntity.builder()
					.nome("X-Salada")
					.preco(24d)
					.produtoCodigo("XPTO")
					.build();
			var pedidoEntity = PedidoEntity.builder()
					.id(1L)
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.dataAlteracao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.itens(List.of(itemPedidoEntity))
					.codigoPedido("PD1")
					.status(StatusPedido.RECEBIDO)
					.total(24d)
					.build();
			when(pedidoMapper.toEntity(any())).thenReturn(pedidoEntity);
			when(itemPedidoJpaRepository.saveAll(anyList())).thenReturn(List.of(itemPedidoEntity));
			when(pedidoJpaRepository.save(any())).thenReturn(pedidoEntity);
			when(pedidoMapper.toDomain(any(PedidoEntity.class))).thenReturn(pedido);
			when(clienteJpaRepository.findByCpf(anyString())).thenReturn(Optional.of(ClienteEntity.builder()
					.nome("Elão dos Foguete")
					.build()));
			Pedido pedidoSalvo = pedidoRepository.salvarPedido(pedido);
			verify(itemPedidoJpaRepository).saveAll(anyList());
			verify(pedidoJpaRepository).save(any());
			assertEquals(pedidoSalvo.getStatusPedido()
					.getStatusAtual(), pedidoEntity.getStatus());
			assertEquals(pedidoSalvo.getCliente()
					.getNome()
					.getValor(), pedidoEntity.getCliente()
					.getNome());
		}

		@Test
		void shouldNotPersistPedido_WhenSalvarPedidoIsCalled_WithoutValidClienteData() {
			var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10);

			var pedido = Pedido.builder()
					.itens(List.of(item))
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.cliente(new Cliente(new Nome("Elão dos Foguete"), new CPF("63656155070"), null))
					.statusPedido(new StatusRecebido())
					.build();

			var itemPedidoEntity = ItemPedidoEntity.builder()
					.nome("X-Salada")
					.preco(24d)
					.produtoCodigo("XPTO")
					.build();
			var pedidoEntity = PedidoEntity.builder()
					.id(1L)
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.dataAlteracao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.itens(List.of(itemPedidoEntity))
					.codigoPedido("PD1")
					.status(StatusPedido.RECEBIDO)
					.total(24d)
					.build();

			when(pedidoMapper.toEntity(any())).thenReturn(pedidoEntity);
			when(clienteJpaRepository.findByCpf(anyString())).thenReturn(Optional.empty());
			var exception = assertThrows(ClienteNaoEncontradoException.class, () -> {
				pedidoRepository.salvarPedido(pedido);
			});
			assertEquals("Cliente não encontrado ao atrelar pedido!", exception.getMessage());
		}

	}

	@Nested
	class FindByCodigo {
		@Test
		void shouldFindByCodigo() {
			var pedidoEntity = PedidoEntity.builder()
					.id(1L)
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.dataAlteracao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.codigoPedido("XPTO")
					.status(StatusPedido.RECEBIDO)
					.total(24d)
					.build();
			var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10);
			var pedido = Pedido.builder()
					.itens(List.of(item))
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.statusPedido(new StatusRecebido())
					.total(24d)
					.build();

			when(pedidoJpaRepository.findByCodigoPedido(anyString())).thenReturn(Optional.of(pedidoEntity));
			when(pedidoMapper.toDomain(any(PedidoEntity.class))).thenReturn(pedido);
			Optional<Pedido> optionalPedido = pedidoRepository.buscarPedidoPeloCodigo("XPTO");
			optionalPedido.ifPresent(value -> assertEquals(value.getTotal(), pedidoEntity.getTotal()));
		}

		@Test
		void shouldThrowsWhenCodigoNotFound() {
			when(pedidoJpaRepository.findByCodigoPedido(anyString())).thenReturn(Optional.empty());
			var exception = assertThrows(PedidoNaoEncontradoException.class, () -> pedidoRepository.buscarPedidoPeloCodigo("XPTO"));
			assertEquals("O Pedido com código XPTO não foi encontrado!", exception.getMessage());
		}
	}

	@Nested
	class AtualizarStatusPedido {
		@Test
		void shouldAtualizarStatusPedido() {
			var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10);
			var pedido = Pedido.builder()
					.itens(List.of(item))
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.cliente(new Cliente(new Nome("Elão dos Foguete"), new CPF("63656155070"), null))
					.statusPedido(new StatusRecebido())
					.codigoPedido("XPTO")
					.build();

			var itemPedidoEntity = ItemPedidoEntity.builder()
					.nome("X-Salada")
					.preco(24d)
					.produtoCodigo("XPTO")
					.build();

			var pedidoEntity = PedidoEntity.builder()
					.id(1L)
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.dataAlteracao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.itens(List.of(itemPedidoEntity))
					.codigoPedido("PD1")
					.status(StatusPedido.EM_ELABORACAO)
					.total(24d)
					.build();
			when(pedidoJpaRepository.findByCodigoPedido(anyString())).thenReturn(Optional.of(pedidoEntity));
			when(pedidoJpaRepository.save(any())).thenReturn(pedidoEntity);
			when(pedidoMapper.toDomain(anyList())).thenReturn(List.of(pedido));
			pedidoRepository.atualizarStatus(pedido);
			verify(pedidoJpaRepository).save(any());
		}
	}

	@Nested
	class DeletarStatusPedido {
		@Test
		void shouldDeleteStatusPedido() {
			var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10);
			var pedido = Pedido.builder()
					.itens(List.of(item))
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.statusPedido(new StatusRecebido())
					.codigoPedido("XPTO")
					.build();

			var itemPedidoEntity = ItemPedidoEntity.builder()
					.nome("X-Salada")
					.preco(24d)
					.produtoCodigo("XPTO")
					.build();

			var pedidoEntity = PedidoEntity.builder()
					.id(1L)
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.dataAlteracao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.itens(List.of(itemPedidoEntity))
					.codigoPedido("PD1")
					.status(StatusPedido.CANCELADO)
					.total(24d)
					.build();
			when(pedidoJpaRepository.findByCodigoPedido(anyString())).thenReturn(Optional.of(pedidoEntity));
			when(pedidoJpaRepository.save(any())).thenReturn(pedidoEntity);
			pedidoRepository.cancelarPedido(pedido);
			verify(pedidoJpaRepository).save(any());
		}
	}

	@Nested
	class BuscarTodosPedidos {
		@Test
		void shouldFindAll() {
			var pedidoEntity1 = PedidoEntity.builder()
					.id(1L)
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.dataAlteracao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.codigoPedido("PD1")
					.status(StatusPedido.RECEBIDO)
					.total(24d)
					.build();
			var pedido = Pedido.builder()
					.build();
			when(pedidoJpaRepository.findByStatusNot(StatusPedido.CANCELADO)).thenReturn(List.of(pedidoEntity1));
			when(pedidoMapper.toDomain(anyList())).thenReturn(List.of(pedido));
			Optional<List<Pedido>> pedidosOptional = pedidoRepository.buscarPedidos(null);
			pedidosOptional.ifPresent(pedidos -> {
				assertEquals(1, pedidos.size());
			});
		}

		@Test
		void shouldFindAllByStatus() {
			var pedidoEntity1 = PedidoEntity.builder()
					.id(1L)
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.dataAlteracao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.codigoPedido("PD1")
					.status(StatusPedido.RECEBIDO)
					.total(24d)
					.build();
			var pedido = Pedido.builder()
					.build();

			when(pedidoJpaRepository.findByStatus(StatusPedido.RECEBIDO)).thenReturn(List.of(pedidoEntity1));
			when(pedidoMapper.toDomain(anyList())).thenReturn(List.of(pedido));
			Optional<List<Pedido>> pedidosOptional = pedidoRepository.buscarPedidos(StatusPedido.RECEBIDO);
			pedidosOptional.ifPresent(pedidos -> {
				assertEquals(1, pedidos.size());
			});
		}

		@Test
		void shouldSearchByStatus() {
			EnumSet<StatusPedido> enumSet = EnumSet.of(StatusPedido.RECEBIDO, StatusPedido.EM_ELABORACAO,
					StatusPedido.PRONTO);
			var pedidoEntity = PedidoEntity.builder()
					.id(1L)
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.dataAlteracao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.codigoPedido("PD1")
					.status(StatusPedido.RECEBIDO)
					.total(24d)
					.build();
			var pedido = Pedido.builder()
					.itens(List.of(mock(ItemPedido.class)))
					.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
					.statusPedido(new StatusRecebido())
					.build();
			when(pedidoJpaRepository.findByStatusInAndDataCancelamentoIsNullOrderByDataCriacaoAsc(enumSet)).thenReturn(
					Collections.singletonList(pedidoEntity));
			when(pedidoMapper.toDomain(anyList())).thenReturn(Collections.singletonList(pedido));
			pedidoRepository.buscarPedidosPorStatus(enumSet);
			verify(pedidoJpaRepository, times(1)).findByStatusInAndDataCancelamentoIsNullOrderByDataCriacaoAsc(enumSet);
		}
	}
}
