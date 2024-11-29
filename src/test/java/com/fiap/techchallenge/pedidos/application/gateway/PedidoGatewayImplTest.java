package com.fiap.techchallenge.pedidos.application.gateway;

import com.fiap.techchallenge.pedidos.application.repository.PedidoRepository;
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
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class PedidoGatewayImplTest {
	@Mock
	private PedidoRepository pedidoRepository;
	@InjectMocks
	private PedidoGatewayImpl pedidoGateway;

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
	void shouldSavePedidoWhenSalvarPedidoIsCalledWithValidData() {
		when(pedidoRepository.salvarPedido(any(Pedido.class))).thenReturn(getPedido());
		assertDoesNotThrow(() -> {
			pedidoGateway.salvarPedido(getPedido());
		});
	}

	@Test
	void shouldReturnPedidoWhenBuscarPedidoPeloCodigoIsCalledWithValidCodigo() {
		when(pedidoRepository.buscarPedidoPeloCodigo(any())).thenReturn(Optional.of(getPedido()));
		assertDoesNotThrow(() -> {
			pedidoGateway.buscarPedidoPeloCodigo("123");
		});
	}

	@Test
	void shouldReturnPedidoWhenAtualizarStatusIsCalledWithValidPedido() {
		when(pedidoRepository.atualizarStatus(any(Pedido.class))).thenReturn(getPedido());
		assertDoesNotThrow(() -> {
			pedidoGateway.atualizarStatus(getPedido());
		});
	}

	@Test
	void shouldCancelPedidoWhenCancelarPedidoIsCalledWithValidPedido() {
		doNothing().when(pedidoRepository)
				.cancelarPedido(any(Pedido.class));
		assertDoesNotThrow(() -> {
			pedidoGateway.cancelarPedido(getPedido());
		});
	}

	@Test
	void shouldReturnPedidosWhenBuscarPedidosIsCalledWithValidStatus() {
		EnumSet<StatusPedido> enumSet = EnumSet.of(StatusPedido.RECEBIDO);
		when(pedidoRepository.buscarPedidosPorStatus(enumSet)).thenReturn(List.of(getPedido()));
		assertDoesNotThrow(() -> {
			pedidoGateway.buscarPedidosPorStatus(enumSet);
		});
	}

	private Pedido getPedido() {
		var item = new ItemPedido(null, "XPTO", new Preco(24.2), 10,"X-Salada" );
		return Pedido.builder()
				.itens(List.of(item))
				.dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
				.cliente(new Cliente(new Nome("Elão dos Foguete"), new CPF("63656155070"), null))
				.statusPedido(new StatusRecebido())
				.codigoPedido("XPTO")
				.build();
	}
}

