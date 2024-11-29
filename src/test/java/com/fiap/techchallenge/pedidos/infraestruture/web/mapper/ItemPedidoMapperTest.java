package com.fiap.techchallenge.pedidos.infraestruture.web.mapper;

import com.fiap.techchallenge.pedidos.application.controller.dto.ProdutoDTO;
import com.fiap.techchallenge.pedidos.application.gateway.ProdutoExternalGateway;
import com.fiap.techchallenge.pedidos.domain.exceptions.ItemPedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.ItemPedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Preco;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.ItemPedidoEntity;
import com.fiap.techchallenge.pedidos.infraestruture.persistence.entities.PedidoEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ItemPedidoMapperTest {
	@InjectMocks
	private ItemPedidoMapper itemPedidoMapper;
	@Mock
	private ProdutoExternalGateway produtoExternalGateway;
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
	void shouldReturnItemPedidoEntityFromDomain() {
		var itemPedido = new ItemPedido("XPTO", "XPTO", new Preco(25d), 1, "X-Salada");

		var produto = ProdutoDTO.builder()
				.tempoPreparoEmMinutos(10)
				.preco(10.0)
				.ativo(true)
				.codigo("XPTO")
				.nome("X-Salada")
				.build();
		when(produtoExternalGateway.buscarProdutoPeloCodigo(anyString())).thenReturn(produto);

		assertDoesNotThrow(() -> {
			itemPedidoMapper.toEntity(List.of(itemPedido));
		});
	}

	@Test
	void shouldThrowExceptionWhenMapperEntityNotFoundProduto() {
		var itemPedido = new ItemPedido("XPTO", "XPTO", new Preco(25d), 1, "X-Salada");

		when(produtoExternalGateway.buscarProdutoPeloCodigo(anyString())).thenReturn(null);

		var exception = assertThrows(ItemPedidoInvalidoException.class, () -> {
			itemPedidoMapper.toEntity(itemPedido);
		});

		assertEquals("Produto não encontrado!", exception.getMessage());
	}

	@Test
	void shouldReturnItemPedidoFromEntity() {
		var itemPedido = ItemPedidoEntity.builder()
				.pedido(PedidoEntity.builder()
						.build())
				.id(1L)
				.nome("X-Salada")
				.produtoCodigo("XPTO")
				.preco(25d)
				.build();
		var produto = ProdutoDTO.builder()
				.tempoPreparoEmMinutos(10)
				.preco(10.0)
				.ativo(true)
				.codigo("XPTO")
				.nome("X-Salada")
				.build();

		when(produtoExternalGateway.buscarProdutoPeloCodigo(anyString())).thenReturn(produto);

		assertDoesNotThrow(() -> {
			itemPedidoMapper.toDomain(List.of(itemPedido));
		});
	}

	@Test
	void shouldThrowExceptionWhenMapperDomainNotFoundProduto() {
		var itemPedido = ItemPedidoEntity.builder()
				.pedido(PedidoEntity.builder()
						.build())
				.id(1L)
				.nome("X-Salada")
				.produtoCodigo("XPTO")
				.preco(25d)
				.build();

		when(produtoExternalGateway.buscarProdutoPeloCodigo(anyString())).thenReturn(null);

		var exception = assertThrows(ItemPedidoInvalidoException.class, () -> {
			itemPedidoMapper.toDomain(itemPedido);
		});

		assertEquals("Produto não encontrado!", exception.getMessage());
	}

}
