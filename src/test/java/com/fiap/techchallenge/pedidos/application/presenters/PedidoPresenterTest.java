package com.fiap.techchallenge.pedidos.application.presenters;

import com.fiap.techchallenge.pedidos.application.controller.dto.ClienteDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.PedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.ProdutoDTO;
import com.fiap.techchallenge.pedidos.application.gateway.ProdutoExternalGateway;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.model.ItemPedido;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.CPF;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Email;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Nome;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Preco;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusRecebido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PedidoPresenterTest {
	private AutoCloseable openMocks;
	@Mock
	private ClientePresenter clientePresenter;
	@Mock
	private ProdutoExternalGateway produtoExternalGateway;
	@InjectMocks
	private PedidoPresenter presenter;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void shouldReturnValidDataWhenCreatePedidoPresenter() {
		var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10);
		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.cliente(new Cliente(new Nome("Elão dos Foguete"), new CPF("63656155070"), null))
				.statusPedido(new StatusRecebido())
				.codigoPedido("XPTO")
				.build();
		when(clientePresenter.toModelView(pedido.getCliente())).thenReturn(new ClienteDTO());
		when(produtoExternalGateway.buscarProdutoPeloCodigo(anyString())).thenReturn(getProduto());
		PedidoDTO dto = presenter.toModelView(pedido);
		assertEquals(dto.getCodigoPedido(), pedido.getCodigoPedido());
	}

	@Test
	void shouldReturnValidDataWhenCreatePedidoPresenterWithoutClient() {
		var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10);
		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.statusPedido(new StatusRecebido())
				.codigoPedido("XPTO")
				.previsaoPreparo(LocalDateTime.of(2024, 1, 1, 0, 0))
				.build();
		when(produtoExternalGateway.buscarProdutoPeloCodigo(anyString())).thenReturn(getProduto());
		PedidoDTO dto = presenter.toModelView(pedido);
		assertEquals(dto.getCodigoPedido(), pedido.getCodigoPedido());
	}

	@Test
	void shouldReturnNullWhenNotValidInstance() {
		Object object = new Object();
		PedidoDTO dto = presenter.toModelView(object);
		assertNull(dto);
	}

	private ProdutoDTO getProduto() {
		return ProdutoDTO.builder()
				.ativo(true)
				.categoria("LANCHE")
				.descricao("Lanche saboroso")
				.nome("X-Salada")
				.preco(24.4)
				.tempoPreparoEmMinutos(10)
				.build();
	}

}
