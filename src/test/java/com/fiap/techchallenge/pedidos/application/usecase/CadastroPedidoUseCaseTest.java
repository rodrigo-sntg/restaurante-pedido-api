package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroItemPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.ProdutoDTO;
import com.fiap.techchallenge.pedidos.application.gateway.ClienteGateway;
import com.fiap.techchallenge.pedidos.application.gateway.PedidoGateway;
import com.fiap.techchallenge.pedidos.application.gateway.ProdutoExternalGateway;
import com.fiap.techchallenge.pedidos.domain.exceptions.ClienteNaoEncontradoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.model.ItemPedido;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.CPF;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Email;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Nome;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Preco;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusAguardandoPagamento;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

public class CadastroPedidoUseCaseTest {
	@Mock
	private PedidoGateway pedidoGateway;
	@Mock
	private ProdutoExternalGateway produtoGateway;
	@Mock
	private ClienteGateway clienteGateway;

	@InjectMocks
	private CadastroPedidoUseCase cadastroPedidoUseCase;
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
	void shouldSalvarPedidoWithCliente() {
		var cadastroItemPedido = new CadastroItemPedidoDTO(null, "XPTO");
		var cadastroPedido = new CadastroPedidoDTO(Collections.singletonList(cadastroItemPedido), "12345678912");
		var cliente = new Cliente(new Nome("Nome"), new CPF("12345678909"), new Email("email@domain.com"));
		var produto = getProduto();
		var item = getItemPedido(produto);

		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.statusPedido(new StatusAguardandoPagamento())
				.cliente(cliente)
				.build();

		when(pedidoGateway.salvarPedido(any(Pedido.class))).thenReturn(pedido);
		when(produtoGateway.buscarProdutoPeloCodigo(any())).thenReturn(produto);
		when(clienteGateway.buscarClientePorCpf(any())).thenReturn(Optional.of(cliente));

		Pedido resposta = cadastroPedidoUseCase.salvar(cadastroPedido);
		verify(pedidoGateway, times(1)).salvarPedido(any(Pedido.class));

		assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, resposta.getStatusAtual());
	}

	@Test
	void shouldSalvarPedidoWithoutCliente() {
		var cadastroItemPedido = new CadastroItemPedidoDTO(null, "XPTO");
		var cadastroPedido = new CadastroPedidoDTO(Collections.singletonList(cadastroItemPedido), null);
		var produto = getProduto();
		var item = getItemPedido(produto);

		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.statusPedido(new StatusAguardandoPagamento())
				.build();

		when(pedidoGateway.salvarPedido(any(Pedido.class))).thenReturn(pedido);
		when(produtoGateway.buscarProdutoPeloCodigo(any())).thenReturn(produto);

		Pedido resposta = cadastroPedidoUseCase.salvar(cadastroPedido);
		verify(pedidoGateway, times(1)).salvarPedido(any(Pedido.class));

		assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, resposta.getStatusAtual());
	}

	@Test
	void shouldThrowsExceptionWhenClienteNotFound() {
		var cadastroItemPedido = new CadastroItemPedidoDTO(null, "XPTO");
		var cadastroPedido = new CadastroPedidoDTO(Collections.singletonList(cadastroItemPedido), "12345678912");
		var cliente = new Cliente(new Nome("Nome"), new CPF("12345678909"), new Email("email@domain.com"));

		var produto = getProduto();
		var item = getItemPedido(produto);

		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.statusPedido(new StatusAguardandoPagamento())
				.cliente(cliente)
				.build();

		when(pedidoGateway.salvarPedido(any(Pedido.class))).thenReturn(pedido);
		when(produtoGateway.buscarProdutoPeloCodigo(any())).thenReturn(produto);
		when(clienteGateway.buscarClientePorCpf(any())).thenReturn(Optional.empty());
		var exception = assertThrows(ClienteNaoEncontradoException.class, () -> {
			cadastroPedidoUseCase.salvar(cadastroPedido);
		});
		verify(pedidoGateway, never()).salvarPedido(any(Pedido.class));
		assertEquals("Cliente não encontrado!", exception.getMessage());
	}

	@Test
	void shouldThrowsExceptionWhenProdutoIsEmpty() {
		var cadastroItemPedido = new CadastroItemPedidoDTO(null, null);
		var cadastroPedido = new CadastroPedidoDTO(Collections.singletonList(cadastroItemPedido), null);

		var produto = getProduto();
		var item = getItemPedido(produto);

		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.statusPedido(new StatusAguardandoPagamento())
				.build();

		when(pedidoGateway.salvarPedido(any(Pedido.class))).thenReturn(pedido);
		when(produtoGateway.buscarProdutoPeloCodigo(any())).thenReturn(produto);
		when(clienteGateway.buscarClientePorCpf(any())).thenReturn(Optional.empty());
		var exception = assertThrows(PedidoInvalidoException.class, () -> {
			cadastroPedidoUseCase.salvar(cadastroPedido);
		});
		verify(pedidoGateway, never()).salvarPedido(any(Pedido.class));
		assertEquals("O produto deve ser informado!", exception.getMessage());
	}

	@Test
	void shouldThrowsExceptionWhenProdutoNotFound() {
		var cadastroItemPedido = new CadastroItemPedidoDTO(null, "XPTO");
		var cadastroPedido = new CadastroPedidoDTO(Collections.singletonList(cadastroItemPedido), null);

		var produto = getProduto();
		var item = new ItemPedido(null, produto.getCodigo(), new Preco(produto.getPreco()),
				produto.getTempoPreparoEmMinutos(), "X-Salada");

		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.statusPedido(new StatusAguardandoPagamento())
				.build();

		when(pedidoGateway.salvarPedido(any(Pedido.class))).thenReturn(pedido);
		when(produtoGateway.buscarProdutoPeloCodigo(any())).thenReturn(null);
		var exception = assertThrows(PedidoInvalidoException.class, () -> {
			cadastroPedidoUseCase.salvar(cadastroPedido);
		});
		verify(pedidoGateway, never()).salvarPedido(any(Pedido.class));
		assertEquals("Produto com o id: XPTO não localizado!", exception.getMessage());
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

	private ItemPedido getItemPedido(ProdutoDTO produto) {
		return new ItemPedido(null, produto.getCodigo(), new Preco(produto.getPreco()),
				produto.getTempoPreparoEmMinutos(), "X-Salada");
	}
}
