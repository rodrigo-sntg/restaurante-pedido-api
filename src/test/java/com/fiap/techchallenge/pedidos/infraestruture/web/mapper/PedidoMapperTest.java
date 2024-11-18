package com.fiap.techchallenge.pedidos.infraestruture.web.mapper;

import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.CPF;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Email;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Nome;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusRecebido;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.ClienteEntity;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.ItemPedidoEntity;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.PedidoEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

public class PedidoMapperTest {
	@InjectMocks
	private PedidoMapper pedidoMapper;
	@Mock
	private ItemPedidoMapper itemPedidoMapper;
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
	void shouldReturnPedidoEntityWhenValidPedidoDomainWithCliente() {
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
				.cliente(ClienteEntity.builder()
						.cpf("12345678909")
						.email("meu@mail.com.br")
						.nome("Nome")
						.build())
				.build();
		when(itemPedidoMapper.toEntity(anyList())).thenReturn(List.of(itemPedidoEntity));

		List<Pedido> pedidos = pedidoMapper.toDomain(List.of(pedidoEntity));
		assertEquals(pedidos.iterator().next().getCodigoPedido(), pedidoEntity.getCodigoPedido());
	}

	@Test
	void shouldReturnPedidoEntityWhenValidPedidoDomainWithoutCliente() {
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
		when(itemPedidoMapper.toEntity(anyList())).thenReturn(List.of(itemPedidoEntity));

		List<Pedido> pedidos = pedidoMapper.toDomain(List.of(pedidoEntity));
		assertEquals(pedidos.iterator().next().getCodigoPedido(), pedidoEntity.getCodigoPedido());
	}

	@Test
	void shouldReturnPedidoDomainWhenValidPedidoEntityWithCliente() {
		var cliente = new Cliente(new Nome("Nome"), new CPF("12345678909"), new Email("meu@teste.com.br"));
		var pedido = Pedido.builder()
				.statusPedido(new StatusRecebido())
				.codigoPedido("XPTO")
				.cliente(cliente)
				.build();
		when(itemPedidoMapper.toEntity(anyList())).thenReturn(new ArrayList<>());
		PedidoEntity entity = pedidoMapper.toEntity(pedido);
		assertEquals(entity.getCodigoPedido(), pedido.getCodigoPedido());
	}

	@Test
	void shouldReturnPedidoDomainWhenValidPedidoEntityWithoutCliente() {
		var pedido = Pedido.builder()
				.statusPedido(new StatusRecebido())
				.codigoPedido("XPTO")
				.build();
		when(itemPedidoMapper.toEntity(anyList())).thenReturn(new ArrayList<>());
		PedidoEntity entity = pedidoMapper.toEntity(pedido);
		assertEquals(entity.getCodigoPedido(), pedido.getCodigoPedido());
	}
}
