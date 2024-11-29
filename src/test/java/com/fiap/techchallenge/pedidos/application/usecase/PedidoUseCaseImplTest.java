package com.fiap.techchallenge.pedidos.application.usecase;

import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroItemPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.CheckoutDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.ProdutoDTO;
import com.fiap.techchallenge.pedidos.application.gateway.ClienteGateway;
import com.fiap.techchallenge.pedidos.application.gateway.PagamentoExternalGateway;
import com.fiap.techchallenge.pedidos.application.gateway.PedidoGateway;
import com.fiap.techchallenge.pedidos.application.gateway.ProdutoExternalGateway;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.ItemPedido;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.IStatusPedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Preco;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusAguardandoPagamento;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusEmElaboracao;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusProcessandoPagamento;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPronto;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusRecebido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PedidoUseCaseImplTest {
	@Mock
	private PedidoGateway pedidoGateway;
	@Mock
	private ProdutoExternalGateway produtoGateway;
	@Mock
	private ClienteGateway clienteGateway;
	@Mock
	private PagamentoExternalGateway pagamentoGateway;

	@InjectMocks
	private PedidoUseCaseImpl pedidoUseCase;
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
	void shouldCreatePedido() {
		var cadastroItemPedido = new CadastroItemPedidoDTO(null, "XPTO");
		var cadastroPedido = new CadastroPedidoDTO(Collections.singletonList(cadastroItemPedido), null);
		var pedido = getPedido(new StatusRecebido());

		CadastroPedidoUseCase cadastroPedidoUseCase = spy(
				new CadastroPedidoUseCase(pedidoGateway, produtoGateway, clienteGateway));

		when(pedidoGateway.salvarPedido(any(Pedido.class))).thenReturn(pedido);
		when(produtoGateway.buscarProdutoPeloCodigo(any())).thenReturn(getProduto());
		doReturn(pedido).when(cadastroPedidoUseCase)
				.salvar(cadastroPedido);

		Pedido resultado = pedidoUseCase.criarPedido(cadastroPedido);
		assertEquals(pedido.getStatusAtual(), resultado.getStatusAtual());
	}

	@Test
	void shouldBuscarPedidos() {
		var pedido = getPedido(new StatusAguardandoPagamento());
		when(pedidoGateway.buscarPedidoPeloCodigo(any())).thenReturn(Optional.of(pedido));

		var resposta = pedidoUseCase.buscarPedidoPeloCodigo("XPTO");
		resposta.ifPresent(p -> {
			assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, p.getStatusAtual());
		});
	}

	@Test
	void shouldBuscarPedidosPorStatus() {
		var pedido = getPedido(new StatusEmElaboracao());
		var enumSet = EnumSet.of(StatusPedido.EM_ELABORACAO, StatusPedido.RECEBIDO, StatusPedido.PRONTO);
		when(pedidoGateway.buscarPedidosPorStatus(enumSet)).thenReturn(Collections.singletonList(pedido));

		var resposta = pedidoUseCase.buscarPedidosPorStatus(enumSet);
		assertEquals(StatusPedido.EM_ELABORACAO, resposta.iterator()
				.next()
				.getStatusAtual());
	}

	@Test
	void shouldBuscarListaPedidos() {

		when(pedidoGateway.buscarPedidos(any(StatusPedido.class))).thenReturn(
				Optional.of(Collections.singletonList(getPedido(new StatusRecebido()))));

		var resposta = pedidoUseCase.buscarPedidos(StatusPedido.AGUARDANDO_PAGAMENTO);
		verify(pedidoGateway, times(1)).buscarPedidos(any(StatusPedido.class));
		resposta.ifPresent(p -> {
			assertEquals(StatusPedido.RECEBIDO, p.iterator()
					.next()
					.getStatusAtual());
		});
	}

	@Test
	void shouldCancelarPedido() {
		doNothing().when(pedidoGateway)
				.cancelarPedido(any(Pedido.class));
		pedidoUseCase.cancelarPedido(getPedido(new StatusRecebido()));

		verify(pedidoGateway, times(1)).cancelarPedido(any(Pedido.class));
	}

	@Test
	void shouldAvancarStatus() {
		var pedido = getPedido(new StatusRecebido());
		when(pedidoGateway.buscarPedidoPeloCodigo(any())).thenReturn(Optional.of(pedido));
		when(pedidoGateway.atualizarStatus(any(Pedido.class))).thenReturn(pedido);
		pedidoUseCase.avancarStatus("XPTO");

		verify(pedidoGateway, times(1)).atualizarStatus(any(Pedido.class));
	}

	@Test
	void shouldAvancarStatus_ThrowsExceptionWhenPedidoNotFound() {
		var pedido = getPedido(new StatusRecebido());
		when(pedidoGateway.buscarPedidoPeloCodigo(any())).thenReturn(Optional.empty());
		when(pedidoGateway.atualizarStatus(any(Pedido.class))).thenReturn(pedido);
		var exception = assertThrows(PedidoInvalidoException.class, () -> {
			pedidoUseCase.avancarStatus("XPTO");
		});

		verify(pedidoGateway, never()).atualizarStatus(any(Pedido.class));
		assertEquals("Pedido não encontrado para o código: (XPTO).", exception.getMessage());
	}

	@Test
	void shouldVoltarStatus() {
		var pedido = getPedido(new StatusEmElaboracao());
		pedido.proximoStatus();
		when(pedidoGateway.buscarPedidoPeloCodigo(any())).thenReturn(Optional.of(pedido));
		when(pedidoGateway.atualizarStatus(any(Pedido.class))).thenReturn(pedido);

		Pedido retorno = pedidoUseCase.voltarStatus("XPTO");

		verify(pedidoGateway, times(1)).atualizarStatus(any(Pedido.class));
		assertEquals(StatusPedido.RECEBIDO, retorno.getStatusAtual());
	}

	@Test
	void shouldVoltarStatus_ThrowsExceptionWhenPedidoNotFound() {
		var pedido = getPedido(new StatusEmElaboracao());
		pedido.proximoStatus();
		when(pedidoGateway.buscarPedidoPeloCodigo(any())).thenReturn(Optional.empty());

		var exception = assertThrows(PedidoInvalidoException.class, () -> {
			pedidoUseCase.voltarStatus("XPTO");
		});
		assertEquals("Pedido não encontrado para o código: (XPTO).", exception.getMessage());
		verify(pedidoGateway, never()).atualizarStatus(any(Pedido.class));
	}

	@Test
	void shouldCheckStatusAguardandoPagamento() {
		var pedido = getPedido(new StatusAguardandoPagamento(), LocalDateTime.of(2024, 1, 1, 0, 0),
				LocalDateTime.of(2024, 1, 1, 0, 0));
		when(pedidoGateway.buscarPedidos(null)).thenReturn(Optional.of(Collections.singletonList(pedido)));
		assertDoesNotThrow(() -> {
			pedidoUseCase.verificarEAtualizarStatusDosPedidos(LocalDateTime.of(2024, 1, 1, 0, 10));
		});
	}

	@Test
	void shouldCheckStatusProcessandoPagamento() {
		var pedido = getPedido(new StatusProcessandoPagamento(), LocalDateTime.of(2024, 1, 1, 0, 0),
				LocalDateTime.of(2024, 1, 1, 0, 0));
		when(pedidoGateway.buscarPedidos(null)).thenReturn(Optional.of(Collections.singletonList(pedido)));
		assertDoesNotThrow(() -> {
			pedidoUseCase.verificarEAtualizarStatusDosPedidos(LocalDateTime.of(2024, 1, 1, 0, 10));
		});
	}

	@Test
	void shouldCheckStatusProcessandoPagamento_WhenTimeGranterThanToday() {
		var pedido = getPedido(new StatusProcessandoPagamento(), LocalDateTime.of(2050, 1, 1, 0, 0),
				LocalDateTime.of(2024, 1, 1, 0, 0));
		when(pedidoGateway.buscarPedidos(null)).thenReturn(Optional.of(Collections.singletonList(pedido)));
		assertDoesNotThrow(() -> {
			pedidoUseCase.verificarEAtualizarStatusDosPedidos(LocalDateTime.of(2024, 1, 1, 0, 10));
		});
	}

	@Test
	void shouldCheckStatusEmElaboracao_WhenTimeGranterThanToday() {
		var pedido = getPedido(new StatusEmElaboracao(), LocalDateTime.of(2024, 1, 1, 0, 0),
				LocalDateTime.of(2025, 1, 1, 0, 0));
		when(pedidoGateway.buscarPedidos(null)).thenReturn(Optional.of(Collections.singletonList(pedido)));
		assertDoesNotThrow(() -> {
			pedidoUseCase.verificarEAtualizarStatusDosPedidos(LocalDateTime.of(2024, 1, 1, 0, 10));
		});
	}

	@Test
	void shouldCheckStatusEmElaboracao() {
		var pedido = getPedido(new StatusEmElaboracao(), LocalDateTime.of(2024, 1, 1, 0, 0),
				LocalDateTime.of(2024, 1, 1, 0, 0));
		when(pedidoGateway.buscarPedidos(null)).thenReturn(Optional.of(Collections.singletonList(pedido)));
		assertDoesNotThrow(() -> {
			pedidoUseCase.verificarEAtualizarStatusDosPedidos(LocalDateTime.of(2024, 1, 1, 0, 10));
		});
	}

	@Test
	void shouldCheckStatusPronto() {
		var pedido = getPedido(new StatusPronto(), LocalDateTime.of(2024, 1, 1, 0, 0),
				LocalDateTime.of(2024, 1, 1, 0, 0));
		when(pedidoGateway.buscarPedidos(null)).thenReturn(Optional.of(Collections.singletonList(pedido)));
		assertDoesNotThrow(() -> {
			pedidoUseCase.verificarEAtualizarStatusDosPedidos(LocalDateTime.of(2024, 1, 1, 0, 10));
		});
	}

	@Test
	void shouldCheckStatusAndDoNothing() {
		getPedido(new StatusEmElaboracao(), LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0));
		when(pedidoGateway.buscarPedidos(null)).thenReturn(Optional.empty());
		assertDoesNotThrow(() -> {
			pedidoUseCase.verificarEAtualizarStatusDosPedidos(LocalDateTime.of(2024, 1, 1, 0, 10));
		});
	}

	@Test
	void shouldFazerCheckout() {
		var checkout = CheckoutDTO.builder()
				.id(1L)
				.payment("CREDIT_CARD")
				.paymentUrl("http://pagamento-url.com")
				.referenceId("XPTO")
				.checkoutId("XPTO")
				.build();
		when(pedidoGateway.buscarPedidoPeloCodigo(anyString())).thenReturn(
				Optional.of(getPedido(new StatusAguardandoPagamento())));
		when(pedidoGateway.atualizarStatus(any(Pedido.class))).thenReturn(getPedido(new StatusAguardandoPagamento()));
		when(pagamentoGateway.fazerCheckout(any(Pedido.class))).thenReturn(checkout);
		CheckoutDTO resposta = pedidoUseCase.checkout("XPTO");
		assertEquals(resposta.getCheckoutId(), checkout.getCheckoutId());
	}

	@Test
	void shouldFazerCheckout_ThrowsExceptionWhenPedidoNotFound() {
		when(pedidoGateway.buscarPedidoPeloCodigo(anyString())).thenReturn(Optional.empty());
		var exception = assertThrows(PedidoInvalidoException.class, () -> {
			pedidoUseCase.checkout("XPTO");
		});
		assertEquals("Pedido não encontrado para o código: (XPTO).", exception.getMessage());
	}

	private Pedido getPedido(IStatusPedido statusPedido) {
		var produto = getProduto();
		var item = getItemPedido(produto);

		return Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.statusPedido(statusPedido)
				.build();
	}

	private Pedido getPedido(IStatusPedido statusPedido, LocalDateTime tempoAtualizacao,
			LocalDateTime previsaoPreparo) {
		var produto = getProduto();
		var item = getItemPedido(produto);

		return Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.dataAlteracao(tempoAtualizacao)
				.statusPedido(statusPedido)
				.previsaoPreparo(previsaoPreparo)
				.build();
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
				produto.getTempoPreparoEmMinutos());
	}
}
