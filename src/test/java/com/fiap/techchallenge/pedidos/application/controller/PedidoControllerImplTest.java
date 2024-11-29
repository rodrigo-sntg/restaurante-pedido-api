package com.fiap.techchallenge.pedidos.application.controller;

import com.fiap.techchallenge.pedidos.application.controller.dto.CadastroPedidoDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.CheckoutDTO;
import com.fiap.techchallenge.pedidos.application.controller.dto.PedidoDTO;
import com.fiap.techchallenge.pedidos.application.presenters.PedidoPresenter;
import com.fiap.techchallenge.pedidos.application.usecase.PedidoUseCase;
import com.fiap.techchallenge.pedidos.domain.exceptions.PedidoInvalidoException;
import com.fiap.techchallenge.pedidos.domain.exceptions.StatusInvalidoException;
import com.fiap.techchallenge.pedidos.domain.model.Cliente;
import com.fiap.techchallenge.pedidos.domain.model.ItemPedido;
import com.fiap.techchallenge.pedidos.domain.model.Pedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.CPF;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Nome;
import com.fiap.techchallenge.pedidos.domain.valueobjects.Preco;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusPedido;
import com.fiap.techchallenge.pedidos.domain.valueobjects.StatusRecebido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PedidoControllerImplTest {
	private AutoCloseable openMocks;
	@InjectMocks
	private PedidoControllerImpl pedidoController;
	@Mock
	private PedidoUseCase pedidoUseCase;
	@Mock
	private PedidoPresenter pedidoPresenter;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void shouldReturnPedidoWhenBuscarPedidoIsCalled() {
		var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10,"XPTO");
		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.cliente(new Cliente(new Nome("Elão dos Foguete"), new CPF("63656155070"), null))
				.statusPedido(new StatusRecebido())
				.codigoPedido("XPTO")
				.build();

		var pedidoDTO = PedidoDTO.builder()
				.codigoPedido("XPTO")
				.build();
		when(pedidoUseCase.buscarPedidos(any())).thenReturn(Optional.of(List.of(pedido)));
		when(pedidoPresenter.toModelView(any(Object.class))).thenReturn(pedidoDTO);
		List<PedidoDTO> pedidos = pedidoController.buscarPedidos(null);
		assertEquals(pedidos.iterator()
				.next()
				.getCodigoPedido(), pedido.getCodigoPedido());
	}

	@Test
	void shouldReturnPedidoWhenBuscarPedidoWithStatusIsCalled() {
		var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10);
		var pedido = Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.cliente(new Cliente(new Nome("Elão dos Foguete"), new CPF("63656155070"), null))
				.statusPedido(new StatusRecebido())
				.codigoPedido("XPTO")
				.build();

		var pedidoDTO = PedidoDTO.builder()
				.codigoPedido("XPTO")
				.build();
		when(pedidoUseCase.buscarPedidos(any())).thenReturn(Optional.of(List.of(pedido)));
		when(pedidoPresenter.toModelView(any(Object.class))).thenReturn(pedidoDTO);
		List<PedidoDTO> pedidos = pedidoController.buscarPedidos("Recebido");
		assertEquals(pedidos.iterator()
				.next()
				.getCodigoPedido(), pedido.getCodigoPedido());
	}

	@Test
	void shouldReturnEmptyPedidoWhenBuscarPedidoNotFoundAnyPedido() {
		when(pedidoUseCase.buscarPedidos(any())).thenReturn(Optional.empty());
		List<PedidoDTO> pedidos = pedidoController.buscarPedidos(null);
		assertTrue(pedidos.isEmpty());
	}

	@Test
	void shouldReturnEmptyPedidoWhenBuscarPedidoNotFoundAnyPedido_WithEmptyValueWasInformed() {
		when(pedidoUseCase.buscarPedidos(any())).thenReturn(Optional.empty());
		List<PedidoDTO> pedidos = pedidoController.buscarPedidos(" ");
		assertTrue(pedidos.isEmpty());
	}

	@Test
	void shouldThrowExceptionWhenBuscarPedidoWithInvalidStatus() {
		when(pedidoUseCase.buscarPedidos(any())).thenThrow(new StatusInvalidoException("O status informado não existe!"));
		var exception = assertThrows(StatusInvalidoException.class, () -> pedidoController.buscarPedidos("INVALID"));
		assertEquals("O status informado não existe!", exception.getMessage());
	}

	@Test
	void shouldBuscarPedidoPeloCodigo() {
		var pedido = Pedido.builder()
				.codigoPedido("XPTO")
				.build();
		var pedidoDTO = PedidoDTO.builder()
				.codigoPedido("XPTO")
				.build();
		when(pedidoUseCase.buscarPedidoPeloCodigo(any())).thenReturn(Optional.of(pedido));
		when(pedidoPresenter.toModelView(any(Object.class))).thenReturn(pedidoDTO);
		var pedidoDTOFound = pedidoController.buscarPedidoPeloCodigo("XPTO");
		pedidoDTOFound.ifPresent(item -> {
			assertEquals(item.getCodigoPedido(), pedidoDTO.getCodigoPedido());
		});
	}

	@Test
	void shouldBuscarPedidosEmProgresso() {
		EnumSet<StatusPedido> statusEmProgresso = EnumSet.of(StatusPedido.RECEBIDO, StatusPedido.EM_ELABORACAO,
				StatusPedido.PRONTO);
		var pedidoDTO = PedidoDTO.builder()
				.codigoPedido("XPTO")
				.status(StatusPedido.RECEBIDO.name())
				.total(25d)
				.build();

		when(pedidoUseCase.buscarPedidosPorStatus(statusEmProgresso)).thenReturn(List.of(new Pedido()));
		when(pedidoPresenter.toModelView(any(Object.class))).thenReturn(pedidoDTO);
		pedidoController.buscarPedidosEmProgresso();
		verify(pedidoPresenter).toModelView(any(Object.class));
		verify(pedidoUseCase).buscarPedidosPorStatus(statusEmProgresso);

	}

	@Test
	void shouldCancelarPedido() {
		var pedido = new Pedido();
		when(pedidoUseCase.buscarPedidoPeloCodigo(any())).thenReturn(Optional.of(pedido));
		pedidoController.cancelarPedido("XPTO");
		verify(pedidoUseCase).cancelarPedido(pedido);
	}

	@Test
	void shouldThrowExceptionWhenCancelarPedidoNotFoundCodigoPedido() {
		when(pedidoUseCase.buscarPedidoPeloCodigo(any())).thenReturn(Optional.empty());
		var exception = assertThrows(PedidoInvalidoException.class, () -> pedidoController.cancelarPedido("XPTO"));
		assertEquals("Pedido não localizado", exception.getMessage());
	}

	@Test
	void shouldAtualizarStatusPedido() {
		var pedido = new Pedido();
		var pedidoDTO = PedidoDTO.builder()
				.codigoPedido("XPTO")
				.status(StatusPedido.RECEBIDO.name())
				.total(25d)
				.build();
		when(pedidoUseCase.avancarStatus(any())).thenReturn(pedido);
		when(pedidoPresenter.toModelView(any(Object.class))).thenReturn(pedidoDTO);
		pedidoController.atualizarStatusPedidoPago("XPTO");
		verify(pedidoUseCase).avancarStatus("XPTO");
		verify(pedidoPresenter).toModelView(any(Object.class));
	}

	@Test
	void shouldVoltarStatus() {
		var pedido = new Pedido();
		var pedidoDTO = PedidoDTO.builder()
				.codigoPedido("XPTO")
				.status(StatusPedido.RECEBIDO.name())
				.total(25d)
				.build();
		when(pedidoUseCase.voltarStatus(any())).thenReturn(pedido);
		when(pedidoPresenter.toModelView(any(Object.class))).thenReturn(pedidoDTO);
		pedidoController.voltarStatus("XPTO");
		verify(pedidoUseCase).voltarStatus("XPTO");
		verify(pedidoPresenter).toModelView(any(Object.class));
	}

	@Test
	void shouldCriarPedido() {
		var pedido = new Pedido();
		var cadastroPedidoDTO = CadastroPedidoDTO.builder()
				.itens(new ArrayList<>())
				.build();
		var pedidoDTO = PedidoDTO.builder()
				.codigoPedido("XPTO")
				.status(StatusPedido.RECEBIDO.name())
				.total(25d)
				.build();
		when(pedidoUseCase.criarPedido(any())).thenReturn(pedido);
		when(pedidoPresenter.toModelView(any(Object.class))).thenReturn(pedidoDTO);
		pedidoController.criarPedido(cadastroPedidoDTO);
		verify(pedidoUseCase).criarPedido(any(CadastroPedidoDTO.class));
		verify(pedidoPresenter).toModelView(any(Object.class));
	}

	@Test
	void shouldFazerCheckout(){
		var checkout = CheckoutDTO.builder()
				.id(1l)
				.payment("CREDIT_CARD")
				.paymentUrl("http://pagamento-url.com")
				.referenceId("XPTO")
				.checkoutId("XPTO")
				.build();

		when(pedidoUseCase.checkout(any())).thenReturn(checkout);
		var checkoutDTO = pedidoController.checkout("XPTO");
		assertEquals(checkoutDTO.getCheckoutId(), checkout.getCheckoutId());
	}
}
